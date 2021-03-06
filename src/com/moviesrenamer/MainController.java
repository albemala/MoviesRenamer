package com.moviesrenamer;

import com.moviesrenamer.model.MovieFile;
import com.moviesrenamer.model.MovieInfo;
import com.moviesrenamer.tasks.FetchTMDBInfoTask;
import com.moviesrenamer.tasks.GuessInfoTask;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainController implements GuessInfoTask.GuessInfoTaskListener, FetchTMDBInfoTask.FetchTMDBInfoTaskListener {

    @FXML
    private MenuButton removeMoviesMenuButton;
    @FXML
    private MenuButton addMoviesMenuButton;
    @FXML
    private SplitPane splitPane;
    @FXML
    private TableView<MovieFile> moviesTableView;
    @FXML
    private TableView<MovieInfo> movieInfoTableView;
    @FXML
    private ProgressBar movieInfoProgress;
    @FXML
    private VBox movieInfoPanel;
    @FXML
    private TextField movieInfoSearchField;

    private FileChooser fileChooser;
    private DirectoryChooser directoryChooser;
    private File lastSelectedFile;
    private MovieFilesManager movieFilesManager;
    private TasksManager tasksManager;


    public MainController() {
        fileChooser = new FileChooser();
        directoryChooser = new DirectoryChooser();
        lastSelectedFile = new File(System.getProperty("user.home"));
        movieFilesManager = new MovieFilesManager();
        tasksManager = new TasksManager();
    }

    @FXML
    public void initialize() {
        // init addMoviesMenuButton items
        {
            ObservableList<MenuItem> addMoviesItems = addMoviesMenuButton.getItems();
            addMoviesItems.clear();
            MenuItem addMoviesItem = new MenuItem("Add movies");
            MenuItem addMoviesInFolderItem = new MenuItem("Add movies in folders");
            MenuItem addMoviesInFolderAndSubFoldersItem = new MenuItem("Add movies in folders AND sub-folders");
            addMoviesItem.setOnAction(this::addMoviesAction);
            addMoviesInFolderItem.setOnAction(this::addMoviesInDirectoryAction);
            addMoviesInFolderAndSubFoldersItem.setOnAction(this::addMoviesInDirectoryTreeAction);
            addMoviesItems.addAll(addMoviesItem, addMoviesInFolderItem, addMoviesInFolderAndSubFoldersItem);
        }
        // init removeMoviesMenuButton items
        {
            ObservableList<MenuItem> removeMoviesItems = removeMoviesMenuButton.getItems();
            removeMoviesItems.clear();
            MenuItem removeSelectedMoviesItem = new MenuItem("Remove selected movie(s)");
            removeSelectedMoviesItem.setOnAction(this::removeSelectedMoviesAction);
            MenuItem removeAllMoviesItem = new MenuItem("Remove ALL movies");
            removeAllMoviesItem.setOnAction(this::removeAllMoviesAction);
            removeMoviesItems.addAll(removeSelectedMoviesItem, removeAllMoviesItem);
        }
        // init moviesTableView
        {
            TableColumn<MovieFile, String> originalNameColumn = new TableColumn<>("Original name");
            originalNameColumn.setCellValueFactory(
                    new PropertyValueFactory<>("originalName")
            );
            TableColumn<MovieFile, String> newNameColumn = new TableColumn<>("New name");
            newNameColumn.setCellValueFactory(
                    new PropertyValueFactory<>("newName")
            );
            moviesTableView.getColumns().clear();
            moviesTableView.getColumns().addAll(originalNameColumn, newNameColumn);
            moviesTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            moviesTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                MovieFile movieFile = observable.getValue();
                if (movieFile != null) {
                    movieInfoPanel.setDisable(false);
                    ObservableList<MovieInfo> movieInfo = movieFile.getMovieInfo();
                    movieInfoTableView.setItems(movieInfo);
                    if (movieInfo.size() > 0) {
                        movieInfoTableView.getSelectionModel().select(movieFile.getSelectedMovieInfo());
                    }
                } else {
                    movieInfoPanel.setDisable(true);
                    movieInfoTableView.getItems().clear();
                    movieInfoPanel.setDisable(true);
                }
                movieInfoSearchField.setText("");
            });
        }
        // init movieInfoTableView
        {
            TableColumn<MovieInfo, String> titleColumn = new TableColumn<>("Title");
            titleColumn.setCellValueFactory(
                    new PropertyValueFactory<>("title")
            );
            TableColumn<MovieInfo, String> originalTitleColumn = new TableColumn<>("Original title");
            originalTitleColumn.setCellValueFactory(
                    new PropertyValueFactory<>("originalTitle")
            );
            TableColumn<MovieInfo, String> releaseDateColumn = new TableColumn<>("Release date");
            releaseDateColumn.setCellValueFactory(
                    new PropertyValueFactory<>("releaseDate")
            );
            TableColumn<MovieInfo, Integer> runtimeColumn = new TableColumn<>("Runtime");
            runtimeColumn.setCellValueFactory(
                    new PropertyValueFactory<>("runtime")
            );
            movieInfoTableView.getColumns().clear();
            movieInfoTableView.getColumns().addAll(titleColumn, originalTitleColumn, releaseDateColumn, runtimeColumn);
            movieInfoTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            movieInfoTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                MovieInfo movieInfo = observable.getValue();
                if (movieInfo != null) {
                    MovieFile movieFile = moviesTableView.getSelectionModel().getSelectedItem();
                    int index = movieFile.getMovieInfo().indexOf(movieInfo);
                    if (movieFile.getSelectedMovieInfo() != index)
                        movieFile.setSelectedMovieInfo(index);
                }
            });
        }
        movieInfoProgress.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        movieInfoProgress.setVisible(false);

        movieInfoPanel.setDisable(true);

        @NotNull ObservableList<MovieFile> movieFiles = movieFilesManager.getMovieFiles();
        bindMovieInfoStateChanged(movieFiles);
        moviesTableView.setItems(movieFiles);

        tasksManager.start();
    }

    public void stop() {
        tasksManager.stop();
    }

    private void addMoviesAction(ActionEvent event) {
        fileChooser.setTitle("Add movies");
        fileChooser.setInitialDirectory(lastSelectedFile);
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Movies", MovieFileUtils.EXTENSIONS)
        );
        List<File> files = fileChooser.showOpenMultipleDialog(ControllerUtils.getWindow(splitPane));
        if (files != null) {
            lastSelectedFile = files.get(0).getParentFile();
            @NotNull List<MovieFile> movieFiles = movieFilesManager.addMovieFilesToList(files);
            bindMovieInfoStateChanged(movieFiles);
            movieFiles.forEach((movieFile) -> startGuessInfoTask(movieFile, movieFile.getOriginalName()));
        }
    }

    private void addMoviesInDirectoryAction(ActionEvent event) {
        directoryChooser.setTitle("Add movies in folders");
        fileChooser.setInitialDirectory(lastSelectedFile);
        File directory = directoryChooser.showDialog(ControllerUtils.getWindow(splitPane));
        if (directory != null) {
            lastSelectedFile = directory;
            @NotNull List<MovieFile> movieFiles = movieFilesManager.addMovieFilesInDirectoryToList(directory);
            bindMovieInfoStateChanged(movieFiles);
            movieFiles.forEach((movieFile) -> startGuessInfoTask(movieFile, movieFile.getOriginalName()));
        }
    }

    private void addMoviesInDirectoryTreeAction(ActionEvent event) {
        directoryChooser.setTitle("Add movies in folders AND sub-folders");
        fileChooser.setInitialDirectory(lastSelectedFile);
        File directory = directoryChooser.showDialog(ControllerUtils.getWindow(splitPane));
        if (directory != null) {
            lastSelectedFile = directory;
            @NotNull List<MovieFile> movieFiles = movieFilesManager.addMovieFilesInDirectoryTreeToList(directory);
            bindMovieInfoStateChanged(movieFiles);
            movieFiles.forEach((movieFile) -> startGuessInfoTask(movieFile, movieFile.getOriginalName()));
        }
    }

    private void removeSelectedMoviesAction(ActionEvent event) {
        ObservableList<MovieFile> selectedItems = moviesTableView.getSelectionModel().getSelectedItems();
        movieFilesManager.removeMovieFiles(selectedItems);
    }

    private void removeAllMoviesAction(ActionEvent event) {
        movieFilesManager.clearMovieFiles();
    }

    @FXML
    public void showRenamingRuleDialog() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("renaming_rule.fxml"));
        try {
            Parent root = fxmlLoader.load();
            RenamingRuleController controller = fxmlLoader.getController();
            Stage stage = new Stage();
            stage.setTitle("Renaming rule");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void renameMoviesAction(ActionEvent event) {
        ObservableList<MovieFile> movieFiles = movieFilesManager.getMovieFiles();
        movieFiles.forEach((movieFile) -> startGuessInfoTask(movieFile, movieFile.getOriginalName()));
    }

    @FXML
    public void searchMovie(ActionEvent event) {
        MovieFile movieFile = moviesTableView.getSelectionModel().getSelectedItem();
        if (movieFile == null) return;
        String searchString = movieInfoSearchField.getText();
        startGuessInfoTask(movieFile, searchString);
    }

    private void bindMovieInfoStateChanged(List<MovieFile> movieFiles) {
        for (MovieFile movieFile : movieFiles) {
            movieFile.getStateProperty().addListener((observable, oldValue, newValue) -> {
                MovieFile selectedMovieFile = moviesTableView.getSelectionModel().getSelectedItem();
                if (selectedMovieFile != null && selectedMovieFile == movieFile) {
                    switch (movieFile.getState()) {
                        case Normal:
                            movieInfoPanel.setDisable(false);
                            movieInfoProgress.setVisible(false);
                            break;
                        case Updating:
                            movieInfoPanel.setDisable(true);
                            movieInfoProgress.setVisible(true);
                            break;
                    }
                }
            });
        }
    }

    private void startGuessInfoTask(MovieFile movieFile, String searchString) {
        movieFile.setState(MovieFile.State.Updating);
        GuessInfoTask guessInfoTask = new GuessInfoTask(movieFile, searchString);
        guessInfoTask.setListener(this);
        tasksManager.addTask(guessInfoTask);
    }

    @Override
    public void onGuessInfoTaskFinished(MovieFile movieFile) {
        FetchTMDBInfoTask fetchTMDBInfoTask = new FetchTMDBInfoTask(movieFile);
        fetchTMDBInfoTask.setListener(this);
        tasksManager.addTask(fetchTMDBInfoTask);
    }

    @Override
    public void onFetchTMDBInfoTaskFinished(MovieFile movieFile) {
        movieFile.setState(MovieFile.State.Normal);
        List<MovieInfo> movieInfoList = movieFile.getMovieInfo();
        for (MovieInfo movieInfo : movieInfoList) {
            System.out.println(movieInfo.title + ", " + movieInfo.releaseDate + ", " + movieInfo.runtime);
        }
    }
}

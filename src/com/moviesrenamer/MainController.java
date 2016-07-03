package com.moviesrenamer;

import com.moviesrenamer.model.MovieFile;
import com.moviesrenamer.model.MovieInfo;
import com.moviesrenamer.tasks.FetchTMDBInfoTask;
import com.moviesrenamer.tasks.GuessInfoTask;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class MainController implements GuessInfoTask.GuessInfoTaskListener, FetchTMDBInfoTask.FetchTMDBInfoTaskListener {

    @FXML
    private MenuButton removeMoviesMenuButton;
    @FXML
    private MenuButton addMoviesMenuButton;
    @FXML
    private SplitPane rootPanel;
    @FXML
    private TableView<MovieFile> moviesTableView;

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
        ObservableList<MenuItem> addMoviesItems = addMoviesMenuButton.getItems();
        addMoviesItems.clear();
        MenuItem addMoviesItem = new MenuItem("Add movies");
        MenuItem addMoviesInFolderItem = new MenuItem("Add movies in folders");
        MenuItem addMoviesInFolderAndSubFoldersItem = new MenuItem("Add movies in folders AND sub-folders");
        addMoviesItem.setOnAction(this::addMoviesAction);
        addMoviesInFolderItem.setOnAction(this::addMoviesInDirectoryAction);
        addMoviesInFolderAndSubFoldersItem.setOnAction(this::addMoviesInDirectoryTreeAction);
        addMoviesItems.addAll(addMoviesItem, addMoviesInFolderItem, addMoviesInFolderAndSubFoldersItem);
        // init removeMoviesMenuButton items
        ObservableList<MenuItem> removeMoviesItems = removeMoviesMenuButton.getItems();
        removeMoviesItems.clear();
        MenuItem removeSelectedMoviesItem = new MenuItem("Remove selected movie(s)");
        removeSelectedMoviesItem.setOnAction(this::removeSelectedMoviesAction);
        MenuItem removeAllMoviesItem = new MenuItem("Remove ALL movies");
        removeAllMoviesItem.setOnAction(this::removeAllMoviesAction);
        removeMoviesItems.addAll(removeSelectedMoviesItem, removeAllMoviesItem);
        // init moviesTableView
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
        moviesTableView.setItems(movieFilesManager.getMovieFiles());
        moviesTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

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
        List<File> files = fileChooser.showOpenMultipleDialog(ControllerUtils.getWindow(rootPanel));
        if (files != null) {
            lastSelectedFile = files.get(0).getParentFile();
            @NotNull List<MovieFile> movieFiles = movieFilesManager.addMovieFilesToList(files);
            startGuessInfoTasks(movieFiles);
        }
    }

    private void addMoviesInDirectoryAction(ActionEvent event) {
        directoryChooser.setTitle("Add movies in folders");
        fileChooser.setInitialDirectory(lastSelectedFile);
        File directory = directoryChooser.showDialog(ControllerUtils.getWindow(rootPanel));
        if (directory != null) {
            lastSelectedFile = directory;
            @NotNull List<MovieFile> movieFiles = movieFilesManager.addMovieFilesInDirectoryToList(directory);
            startGuessInfoTasks(movieFiles);
        }
    }

    private void addMoviesInDirectoryTreeAction(ActionEvent event) {
        directoryChooser.setTitle("Add movies in folders AND sub-folders");
        fileChooser.setInitialDirectory(lastSelectedFile);
        File directory = directoryChooser.showDialog(ControllerUtils.getWindow(rootPanel));
        if (directory != null) {
            lastSelectedFile = directory;
            @NotNull List<MovieFile> movieFiles = movieFilesManager.addMovieFilesInDirectoryTreeToList(directory);
            startGuessInfoTasks(movieFiles);
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
    public void renameMoviesAction(ActionEvent event) {
        ObservableList<MovieFile> movieFiles = movieFilesManager.getMovieFiles();
        startGuessInfoTasks(movieFiles);
    }

    private void startGuessInfoTasks(List<MovieFile> movieFiles) {
        for (MovieFile movieFile : movieFiles) {
            GuessInfoTask guessInfoTask = new GuessInfoTask(movieFile);
            guessInfoTask.setListener(this);
            tasksManager.addTask(guessInfoTask);
        }
    }

    @Override
    public void onGuessInfoTaskFinished(MovieFile movieFile) {
        FetchTMDBInfoTask fetchTMDBInfoTask = new FetchTMDBInfoTask(movieFile);
        fetchTMDBInfoTask.setListener(this);
        tasksManager.addTask(fetchTMDBInfoTask);
    }

    @Override
    public void onFetchTMDBInfoTaskFinished(MovieFile movieFile) {
        List<MovieInfo> movieInfo = movieFile.getMovieInfo();
        for (MovieInfo info : movieInfo) {
            System.out.println(info.title + ", " + info.releaseDate + ", " + info.runtime);
        }
    }
}

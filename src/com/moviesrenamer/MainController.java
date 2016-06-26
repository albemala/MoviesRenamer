package com.moviesrenamer;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class MainController {

    @FXML
    private MenuButton removeMoviesMenuButton;
    @FXML
    private MenuButton addMoviesMenuButton;
    @FXML
    private GridPane rootPanel;
    @FXML
    private TableView<MovieFile> moviesTableView;

    private FileChooser fileChooser;
    private DirectoryChooser directoryChooser;
    private File lastSelectedFile;
    private MovieFilesManager movieFilesManager;
    private MovieFilesInfoFetcher movieFilesInfoFetcher;

    public MainController() {
        fileChooser = new FileChooser();
        directoryChooser = new DirectoryChooser();
        lastSelectedFile = new File(System.getProperty("user.home"));
        movieFilesManager = new MovieFilesManager();
        movieFilesInfoFetcher = new MovieFilesInfoFetcher();
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
        }
    }

    private void addMoviesInDirectoryAction(ActionEvent event) {
        directoryChooser.setTitle("Add movies in folders");
        fileChooser.setInitialDirectory(lastSelectedFile);
        File directory = directoryChooser.showDialog(ControllerUtils.getWindow(rootPanel));
        if (directory != null) {
            lastSelectedFile = directory;
            @NotNull List<MovieFile> movieFiles = movieFilesManager.addMovieFilesInDirectoryToList(directory);
        }
    }

    private void addMoviesInDirectoryTreeAction(ActionEvent event) {
        directoryChooser.setTitle("Add movies in folders AND sub-folders");
        fileChooser.setInitialDirectory(lastSelectedFile);
        File directory = directoryChooser.showDialog(ControllerUtils.getWindow(rootPanel));
        if (directory != null) {
            lastSelectedFile = directory;
            @NotNull List<MovieFile> movieFiles = movieFilesManager.addMovieFilesInDirectoryTreeToList(directory);
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
        movieFilesInfoFetcher.fetchMovieFilesInfo(movieFiles);
    }
}

package com.moviesrenamer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

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
    private ObservableList<MovieFile> movieFiles;

    public MainController() {
        fileChooser = new FileChooser();
        directoryChooser = new DirectoryChooser();
        lastSelectedFile = new File(System.getProperty("user.home"));
        movieFiles = FXCollections.observableArrayList(
                new MovieFile(new File("/Users/albertomalagoli/Downloads/Night of the Living Dead[1968]DvDrip[Eng]-Stealthmaster.avi")),
                new MovieFile(new File("/Users/albertomalagoli/Downloads/Paura e delirio a Las Vegas (1998, 118).avi")),
                new MovieFile(new File("/Users/albertomalagoli/Downloads/Ricomincio Da Capo.avi"))
        );
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
        addMoviesInFolderItem.setOnAction(this::addMoviesInFolderAction);
        addMoviesInFolderAndSubFoldersItem.setOnAction(this::addMoviesInFolderAndSubFoldersAction);
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
        moviesTableView.setItems(movieFiles);
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
            for (File file : files) {
                movieFiles.add(new MovieFile(file));
            }
        }
    }

    private void addMoviesInFolderAction(ActionEvent event) {
        directoryChooser.setTitle("Add movies in folders");
        fileChooser.setInitialDirectory(lastSelectedFile);
        File directory = directoryChooser.showDialog(ControllerUtils.getWindow(rootPanel));
        if (directory != null) {
            lastSelectedFile = directory;
            try {
                Stream<Path> paths = Files.list(directory.toPath());
                for (Object o : paths.toArray()) {
                    String absolutePath = o.toString();
                    File file = new File(absolutePath);
                    if (MovieFileUtils.isMovieFile(file)) {
                        movieFiles.add(new MovieFile(file));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addMoviesInFolderAndSubFoldersAction(ActionEvent event) {
        System.out.println("add movies in folders and subfolders");
        directoryChooser.setTitle("Add movies in folders AND sub-folders");
        fileChooser.setInitialDirectory(lastSelectedFile);
        File directory = directoryChooser.showDialog(ControllerUtils.getWindow(rootPanel));
        if (directory != null) {
            lastSelectedFile = directory;
            System.out.println(directory.getAbsolutePath());
            // todo: finire
        }
    }

    private void removeSelectedMoviesAction(ActionEvent event) {
        ObservableList<MovieFile> selectedItems = moviesTableView.getSelectionModel().getSelectedItems();
        movieFiles.removeAll(selectedItems);
    }

    private void removeAllMoviesAction(ActionEvent event) {
        movieFiles.clear();
    }
}

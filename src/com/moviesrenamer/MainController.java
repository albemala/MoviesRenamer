package com.moviesrenamer;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

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
    private TableView<String> moviesTableView;
    private FileChooser fileChooser;
    private DirectoryChooser directoryChooser;
    private File lastSelectedFile;

    public MainController() {
        fileChooser = new FileChooser();
        directoryChooser = new DirectoryChooser();
        lastSelectedFile = new File(System.getProperty("user.home"));
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
    }

    private void addMoviesAction(ActionEvent event) {
        System.out.println("add movies");
        fileChooser.setTitle("Add movies");
        fileChooser.setInitialDirectory(lastSelectedFile);
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Movies", "*.avi", "*.mp4", "*.mkv")
        );
        List<File> files = fileChooser.showOpenMultipleDialog(ControllerUtils.getWindow(rootPanel));
        if (files != null) {
            lastSelectedFile = files.get(0).getParentFile();
            for (File file : files) {
                System.out.println(file.getAbsolutePath());
            }
        }
    }

    private void addMoviesInFolderAction(ActionEvent event) {
        System.out.println("add movies in folders");
        directoryChooser.setTitle("Add movies in folders");
        fileChooser.setInitialDirectory(lastSelectedFile);
        File file = directoryChooser.showDialog(ControllerUtils.getWindow(rootPanel));
        if (file != null) {
            lastSelectedFile = file;
            System.out.println(file.getAbsolutePath());
        }
    }

    private void addMoviesInFolderAndSubFoldersAction(ActionEvent event) {
        System.out.println("add movies in folders and subfolders");
        directoryChooser.setTitle("Add movies in folders AND sub-folders");
        fileChooser.setInitialDirectory(lastSelectedFile);
        File file = directoryChooser.showDialog(ControllerUtils.getWindow(rootPanel));
        if (file != null) {
            lastSelectedFile = file;
            System.out.println(file.getAbsolutePath());
        }
    }

    private void removeAllMoviesAction(ActionEvent event) {
        System.out.println("remove all movies");
    }

    private void removeSelectedMoviesAction(ActionEvent event) {
        System.out.println("remove movies");
    }
}

package com.moviesrenamer;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

public class MainController {

    @FXML
    private MenuButton removeMoviesMenuButton;

    public MainController() {
    }

    @FXML
    public void initialize() {
        // init removeMoviesMenuButton items
        ObservableList<MenuItem> items = removeMoviesMenuButton.getItems();
        items.clear();
        MenuItem removeSelectedMoviesItem = new MenuItem("Remove selected movie(s)");
        removeSelectedMoviesItem.setOnAction(this::removeSelectedMoviesAction);
        MenuItem removeAllMoviesItem = new MenuItem("Remove ALL movies");
        removeAllMoviesItem.setOnAction(this::removeAllMoviesAction);
        items.addAll(removeSelectedMoviesItem, removeAllMoviesItem);
    }

    private void removeAllMoviesAction(ActionEvent event) {
        System.out.println("remove all movies");
    }

    private void removeSelectedMoviesAction(ActionEvent event) {
        System.out.println("remove movies");
    }

    @FXML
    public void addMoviesButtonAction(ActionEvent event) {
        System.out.println("add movies");
    }
}

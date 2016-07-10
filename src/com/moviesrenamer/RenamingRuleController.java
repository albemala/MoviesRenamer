package com.moviesrenamer;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class RenamingRuleController {

    @FXML
    private GridPane rootPane;
    @FXML
    private ListView<String> renamingRuleListView;

    public RenamingRuleController() {
    }

    @FXML
    public void initialize() {

    }

    @FXML
    public void removeSelectedRule() {

    }

    @FXML
    public void clearRule() {

    }

    @FXML
    public void addTitle() {
    }

    @FXML
    public void addOriginalTitle() {
    }

    @FXML
    public void addYear() {
    }

    @FXML
    public void addRuntime() {
    }

    @FXML
    public void addLanguage() {
    }

    @FXML
    public void addRoundBrackets() {
    }

    @FXML
    public void addSquareBrackets() {
    }

    @FXML
    public void close() {
        Stage stage = (Stage) ControllerUtils.getWindow(rootPane);
        stage.close();
    }
}

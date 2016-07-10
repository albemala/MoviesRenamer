package com.moviesrenamer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class RenamingRuleController {

    @FXML
    private GridPane rootPane;
    @FXML
    private ListView<String> renamingRuleListView;

    private ObservableList<String> renamingRule;

    public RenamingRuleController() {
        renamingRule = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        renamingRuleListView.setItems(renamingRule);
    }

    @FXML
    public void removeSelectedRule() {

    }

    @FXML
    public void clearRule() {
        renamingRule.clear();
    }

    @FXML
    public void addTitle() {
        renamingRule.add("title");
    }

    @FXML
    public void addOriginalTitle() {
        renamingRule.add("originalTitle");
    }

    @FXML
    public void addYear() {
        renamingRule.add("year");
    }

    @FXML
    public void addRuntime() {
        renamingRule.add("runtime");
    }

    @FXML
    public void addLanguage() {
        renamingRule.add("language");
    }

    @FXML
    public void addRoundBrackets() {
        renamingRule.add("(");
        renamingRule.add(")");
    }

    @FXML
    public void addSquareBrackets() {
        renamingRule.add("[");
        renamingRule.add("]");
    }

    @FXML
    public void close() {
        Stage stage = (Stage) ControllerUtils.getWindow(rootPane);
        stage.close();
    }
}

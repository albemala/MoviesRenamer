package com.moviesrenamer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class MainApplication extends Application {

    public static final String APP_NAME = "MoviesRenamer";
    private MainController mainController;

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("main.fxml"));
        Parent root = fxmlLoader.load();

        mainController = fxmlLoader.getController();

        primaryStage.setTitle(APP_NAME);
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        mainController.stop();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

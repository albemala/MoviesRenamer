package com.moviesrenamer;

import javafx.scene.Node;
import javafx.stage.Window;

class ControllerUtils {

    static Window getWindow(Node node) {
        return node.getScene().getWindow();
    }
}

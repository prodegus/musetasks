package prodegus.musetasks.utils;

import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;

import static prodegus.musetasks.utils.DateTime.times;

public class Nodes {
    public static ComboBox<String> timeComboBox(int width, int height, String prompt) {
        ComboBox<String> timeComboBox = new ComboBox<>(FXCollections.observableArrayList(times(8, 23)));
        timeComboBox.setPrefWidth(width);
        timeComboBox.setPrefHeight(height);
        timeComboBox.setPromptText(prompt);
        return timeComboBox;
    }

    public static void show(Node node) {
        node.setVisible(true);
        node.setManaged(true);
    }

    public static void hide(Node node) {
        node.setVisible(false);
        node.setManaged(false);
    }
}

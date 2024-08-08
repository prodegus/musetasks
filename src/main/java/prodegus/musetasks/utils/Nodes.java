package prodegus.musetasks.utils;

import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;

import java.util.ArrayList;

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

    public static ArrayList<Node> getAllNodes(Parent root) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        addAllDescendants(root, nodes);
        return nodes;
    }

    private static void addAllDescendants(Parent parent, ArrayList<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof Parent)
                addAllDescendants((Parent)node, nodes);
        }
    }
}

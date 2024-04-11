package prodegus.musetasks.contacts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static prodegus.musetasks.database.Database.TASK_TABLE;
import static prodegus.musetasks.database.Database.connect;

public class TaskModel {

    public static ObservableList<Task> getTaskList() {
        ObservableList<Task> tasks = FXCollections.observableArrayList();
        refreshTaskList(tasks);
        return tasks;
    }

    public static void refreshTaskList(ObservableList<Task> tasks) {
        String sql = "SELECT * FROM " + TASK_TABLE;

        tasks.clear();
        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                Task task = new Task();
                task.setAttributes(rs);
                tasks.add(task);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Task getTask(String id) {
        String sql = "SELECT * FROM " + TASK_TABLE + " WHERE id = " + id;
        Task task = new Task();

        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                task.setAttributes(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return task;
    }

}

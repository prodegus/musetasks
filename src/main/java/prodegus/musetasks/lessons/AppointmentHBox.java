package prodegus.musetasks.lessons;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import prodegus.musetasks.appointments.Appointment;
import prodegus.musetasks.school.Holiday;

import java.time.LocalDate;

import static prodegus.musetasks.school.HolidayModel.getHoliday;
import static prodegus.musetasks.utils.Nodes.timeComboBox;

public class AppointmentHBox extends HBox {
    SimpleIntegerProperty rowIndex = new SimpleIntegerProperty();
    Label rowNumberLabel;
    DatePicker datePicker;
    ComboBox<String> timeComboBox;
    Button deleteButton;
    Label holidayInfoLabel;

    public int getRowIndex() {
        return rowIndex.get();
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex.set(rowIndex);
    }

    public AppointmentHBox(int rowIndex) {
        this.setRowIndex(rowIndex);
        this.rowNumberLabel = new Label((this.getRowIndex() + 1) + ". Termin");
        this.datePicker = new DatePicker();
        this.timeComboBox = timeComboBox(80, 25, "Uhrzeit");
        this.deleteButton = new Button("X");
        this.holidayInfoLabel = new Label();

        datePicker.setPromptText("Datum");
        datePicker.setPrefWidth(100);
        datePicker.valueProperty().addListener(e -> {
            LocalDate date = datePicker.getValue();
            Holiday holiday = getHoliday(date);
            holidayInfoLabel.setText(holiday == null ? "" : "Hinweis: " + holiday.getDescription());
        });

        timeComboBox.setPrefWidth(92);
        timeComboBox.setEditable(true);

        deleteButton.setPrefWidth(25);
        deleteButton.setOnAction(e -> {
            VBox aptVBox = (VBox) this.getParent();
            aptVBox.getChildren().remove(this);
            for (int i = this.getRowIndex(); i < aptVBox.getChildren().size(); i++) {
                AppointmentHBox appointmentHBox = (AppointmentHBox) aptVBox.getChildren().get(i);
                appointmentHBox.setRowIndex(i);
                appointmentHBox.rowNumberLabel.setText((i + 1) + ". Termin");
            }
        });

        this.setSpacing(10);
        this.setAlignment(Pos.CENTER_LEFT);
        this.getChildren().addAll(rowNumberLabel, datePicker, timeComboBox, deleteButton, holidayInfoLabel);
    }
}
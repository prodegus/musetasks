package prodegus.musetasks.ui;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import prodegus.musetasks.appointments.Appointment;
import prodegus.musetasks.school.Holiday;

import java.text.BreakIterator;
import java.time.LocalDate;
import java.util.StringJoiner;

import static prodegus.musetasks.appointments.Appointment.CATEGORY_LESSON_REGULAR;
import static prodegus.musetasks.school.HolidayModel.getHoliday;
import static prodegus.musetasks.utils.DateTime.toTime;
import static prodegus.musetasks.utils.Nodes.timeComboBox;

public class AppointmentHBox extends HBox {
    private Appointment appointment;
    private SimpleIntegerProperty rowIndex = new SimpleIntegerProperty();
    private Label rowNumberLabel;
    private DatePicker datePicker;
    private ComboBox<String> timeComboBox;
    private Button deleteButton;
    private Label holidayInfoLabel;

    public Appointment getAppointment() {
        String oldDescription = appointment.getDescription();
        String holidayInfo = this.getHolidayInfoLabel().getText();
        StringJoiner newDescription = new StringJoiner(", ");
        if (!oldDescription.isBlank()) newDescription.add(oldDescription);
        if (!oldDescription.contains(holidayInfo)) newDescription.add(holidayInfo);
        appointment.setDate(this.getDatePicker().getValue());
        appointment.setTime(toTime(this.getTimeComboBox().getValue()));
        appointment.setDescription(newDescription.toString());
        if (appointment.getId() == 0) appointment.setCategory(CATEGORY_LESSON_REGULAR);
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public int getRowIndex() {
        return rowIndex.get();
    }
    public SimpleIntegerProperty rowIndexProperty() {
        return rowIndex;
    }
    public void setRowIndex(int rowIndex) {
        this.rowIndex.set(rowIndex);
    }

    public Label getRowNumberLabel() {
        return rowNumberLabel;
    }
    public void setRowNumberLabel(Label rowNumberLabel) {
        this.rowNumberLabel = rowNumberLabel;
    }

    public DatePicker getDatePicker() {
        return datePicker;
    }
    public void setDatePicker(DatePicker datePicker) {
        this.datePicker = datePicker;
    }

    public ComboBox<String> getTimeComboBox() {
        return timeComboBox;
    }
    public void setTimeComboBox(ComboBox<String> timeComboBox) {
        this.timeComboBox = timeComboBox;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }
    public void setDeleteButton(Button deleteButton) {
        this.deleteButton = deleteButton;
    }

    public Label getHolidayInfoLabel() {
        return holidayInfoLabel;
    }
    public void setHolidayInfoLabel(Label holidayInfoLabel) {
        this.holidayInfoLabel = holidayInfoLabel;
    }


    public AppointmentHBox(int rowIndex, Appointment appointment) {
        this(rowIndex);
        this.setAppointment(appointment);
        this.datePicker.setValue(appointment.getDate());
        this.timeComboBox.setValue(appointment.getTime().toString() + " Uhr");
        if (!appointment.getDescription().isBlank()) holidayInfoLabel.setText(appointment.getDescription());
    }

    public AppointmentHBox(int rowIndex) {
        this.setAppointment(new Appointment());
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
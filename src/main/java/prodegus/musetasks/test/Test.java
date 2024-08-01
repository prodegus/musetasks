package prodegus.musetasks.test;


import javafx.scene.layout.HBox;
import prodegus.musetasks.lessons.AppointmentHBox;

public class Test {

    public static void main(String[] args) {
        AppointmentHBox appointmentHBox = new AppointmentHBox(0);
        System.out.println(appointmentHBox.getClass().getSimpleName());
    }

}
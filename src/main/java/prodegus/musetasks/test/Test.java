package prodegus.musetasks.test;


import javafx.beans.property.SimpleIntegerProperty;
import prodegus.musetasks.ui.AppointmentHBox;

public class Test {

    public static void main(String[] args) {

        SimpleIntegerProperty test = new SimpleIntegerProperty();
        System.out.println(test.get());

    }

    static void checkString(String original) {
        boolean hasSuffix = original.endsWith(".db");
        String result = original + (hasSuffix ? "" : ".db");
        System.out.println("Original String: " + original);
        System.out.println("endsWith(\".db\"): " + hasSuffix);
        System.out.println("result: " + result);
        System.out.println("--------------------------------");
    }

}
package prodegus.musetasks.test;


import prodegus.musetasks.ui.AppointmentHBox;

public class Test {

    public static void main(String[] args) {

        String a = "abcdef";
        String b = "abcdef.db";
        String c = "ab";
        String d = "a.db";
        String e = "a";
        String f = "";

        checkString(a);
        checkString(b);
        checkString(c);
        checkString(d);
        checkString(e);
        checkString(f);
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
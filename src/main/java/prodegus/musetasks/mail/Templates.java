package prodegus.musetasks.mail;

import prodegus.musetasks.appointments.Appointment;
import prodegus.musetasks.contacts.Teacher;
import prodegus.musetasks.lessons.Lesson;

import java.util.StringJoiner;

import static prodegus.musetasks.appointments.AppointmentModel.changes;
import static prodegus.musetasks.contacts.TeacherModel.getTeacherFromDB;
import static prodegus.musetasks.lessons.LessonModel.getLessonFromDB;
import static prodegus.musetasks.school.School.SCHOOL_NAME;
import static prodegus.musetasks.utils.DateTime.fullDateTimeString;

public class Templates {
    public static String templateRescheduled() {
        StringJoiner message = new StringJoiner("\n\n");

        message.add("Guten Tag,");
        message.add("der Unterricht am [Datum, Uhrzeit] bei [Lehrer] muss [Begründung?] abgesagt werden.");
        message.add("Der geplante Nachholtermin ist am [Datum, Uhrzeit], Ort: [Standort, Raum]");
        message.add("Bitte informieren Sie Ihren Lehrer, ob Sie bzw. Ihr Kind den Nachholtermin wahrnehmen können.");
        message.add("Falls Sie Fragen haben, sprechen Sie uns bitte an. Wir sind gerne für Sie da.");
        message.add("Mit freundlichen Grüßen\n" + SCHOOL_NAME);

        return message.toString();
    }

    public static String templateToReschedule() {
        StringJoiner message = new StringJoiner("\n\n");

        message.add("Guten Tag,");
        message.add("der Unterricht am [Datum, Uhrzeit] bei [Lehrer] muss [Begründung?] abgesagt werden.");
        message.add("Ihr zuständiger Lehrer in Kürze wird sich in Kürze mit Ihnen in Verbindung setzen, um einen " +
                "Ersatztermin mit Ihnen zu vereinbaren.");
        message.add("Bitte informieren Sie Ihren Lehrer, ob Sie bzw. Ihr Kind den Nachholtermin wahrnehmen können.");
        message.add("Falls Sie Fragen haben, sprechen Sie uns bitte an. Wir sind gerne für Sie da.");
        message.add("Mit freundlichen Grüßen\n" + SCHOOL_NAME);

        return message.toString();
    }

    public static String templateDropped() {
        StringJoiner message = new StringJoiner("\n\n");

        message.add("Guten Tag,");
        message.add("der Unterricht am [Datum, Uhrzeit] bei [Lehrer] muss [Begründung?] abgesagt werden.");
        message.add("Falls Sie Fragen haben, sprechen Sie uns bitte an. Wir sind gerne für Sie da.");
        message.add("Mit freundlichen Grüßen\n" + SCHOOL_NAME);

        return message.toString();
    }

    public static String templateChanged() {
        StringJoiner message = new StringJoiner("\n\n");

        message.add("Guten Tag,");
        message.add("wir möchten Sie über eine Änderung zum Unterricht am [Datum, Uhrzeit] bei [Lehrer] informieren:");
        message.add("[Änderungen]");
        message.add("Falls Sie Fragen haben, sprechen Sie uns bitte an. Wir sind gerne für Sie da.");
        message.add("Mit freundlichen Grüßen\n" + SCHOOL_NAME);

        return message.toString();
    }

    public static String lessonRescheduled(Appointment oldAppointment, Appointment newAppointment, String reason) {
        Lesson lesson = getLessonFromDB(oldAppointment.getLessonId());
        Teacher teacher = getTeacherFromDB(lesson.getTeacherId());
        boolean illness = reason.equals("Krankheit (Schüler)") || reason.equals("Krankheit (Lehrer)");

        StringJoiner message = new StringJoiner("\n\n");

        message.add("Guten Tag,");
        message.add("der Unterricht am " + fullDateTimeString(oldAppointment.getDate(), oldAppointment.getTime()) +
                " bei " + teacher.name() + " muss" + (illness ? " krankheitsbedingt " : " ") + "abgesagt werden.");
        message.add("Der geplante Nachholtermin ist am " + fullDateTimeString(newAppointment.getDate(),
                newAppointment.getTime()) + ", Ort: " + newAppointment.locationRoom());
        message.add("Bitte informieren Sie Ihren Lehrer, ob Sie bzw. Ihr Kind den Nachholtermin wahrnehmen können.");
        message.add("Falls Sie Fragen haben, sprechen Sie uns bitte an. Wir sind gerne für Sie da.");
        message.add("Mit freundlichen Grüßen\n" + SCHOOL_NAME);

        return message.toString();
    }

    public static String lessonToReschedule(Appointment oldAppointment, String reason) {
        Lesson lesson = getLessonFromDB(oldAppointment.getLessonId());
        Teacher teacher = getTeacherFromDB(lesson.getTeacherId());
        boolean illness = reason.equals("Krankheit (Schüler)") || reason.equals("Krankheit (Lehrer)");

        StringJoiner message = new StringJoiner("\n\n");

        message.add("Guten Tag,");
        message.add("der Unterricht am " + fullDateTimeString(oldAppointment.getDate(), oldAppointment.getTime()) +
                " bei " + teacher.name() + " muss" + (illness ? " krankheitsbedingt " : " ") + "abgesagt werden.");
        message.add("Ihr zuständiger Lehrer in Kürze wird sich in Kürze mit Ihnen in Verbindung setzen, um einen " +
                "Ersatztermin mit Ihnen zu vereinbaren.");
        message.add("Falls Sie Fragen haben, sprechen Sie uns bitte an. Wir sind gerne für Sie da.");
        message.add("Mit freundlichen Grüßen\n" + SCHOOL_NAME);

        return message.toString();
    }

    public static String lessonDropped(Appointment oldAppointment, String reason) {
        Lesson lesson = getLessonFromDB(oldAppointment.getLessonId());
        Teacher teacher = getTeacherFromDB(lesson.getTeacherId());
        boolean illness = reason.equals("Krankheit (Schüler)") || reason.equals("Krankheit (Lehrer)");

        StringJoiner message = new StringJoiner("\n\n");

        message.add("Guten Tag,");
        message.add("der Unterricht am " + fullDateTimeString(oldAppointment.getDate(), oldAppointment.getTime()) +
                " bei " + teacher.name() + " muss" + (illness ? " krankheitsbedingt " : " ") + "abgesagt werden.");
        message.add("Falls Sie Fragen haben, sprechen Sie uns bitte an. Wir sind gerne für Sie da.");
        message.add("Mit freundlichen Grüßen\n" + SCHOOL_NAME);

        return message.toString();
    }

    public static String lessonChanged(Appointment originalAppointment, Appointment changedAppointment) {
        Lesson lesson = getLessonFromDB(originalAppointment.getLessonId());
        Teacher teacher = getTeacherFromDB(lesson.getTeacherId());

        StringJoiner message = new StringJoiner("\n\n");

        message.add("Guten Tag,");
        message.add("wir möchten Sie über eine Änderung zum Unterricht am " +
                fullDateTimeString(originalAppointment.getDate(), originalAppointment.getTime()) +
                " bei " + teacher.name() + " informieren:");
        message.add((changes(originalAppointment, changedAppointment)));
        message.add("Falls Sie Fragen haben, sprechen Sie uns bitte an. Wir sind gerne für Sie da.");
        message.add("Mit freundlichen Grüßen\n" + SCHOOL_NAME);

        return message.toString();
    }
}

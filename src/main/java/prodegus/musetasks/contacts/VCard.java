package prodegus.musetasks.contacts;

import java.io.*;
import java.util.StringJoiner;

import static prodegus.musetasks.login.Settings.*;
import static prodegus.musetasks.login.Settings.getMailPassword;
import static prodegus.musetasks.mail.TLSEmail.sendMail;

public class VCard {

    public static void main(String[] args) {
        Contact contact = new Contact();
        contact.setFirstName("John");
        contact.setLastName("Doe");
        contact.setPhone("+49 176 12345678");
        contact.setEmail("johndoe@gmail.com");

        File vCard = vCard(contact, "Vater von Jack Doe");

//        sendMail("daniel.hanelt@gmail.com", "Ohne Anhang", "Nur Text");
//        sendMail("daniel.hanelt@gmail.com", "", "Visitenkarte", "Siehe Anhang!", getMailSender(), vCard);
    }

    public static File vCard(Contact contact, String description) {
        File file = new File(contact.name() + ".vcf");
        StringJoiner text = new StringJoiner("\n");

        text.add("BEGIN:VCARD");
        text.add("VERSION:4.0");
        text.add("N:" + contact.getLastName() + ";" + contact.getFirstName() + ";;;");
        text.add("FN:" + contact.name());
        if (!description.isBlank()) text.add("TITLE:" + description);
        if (!contact.getPhone().isBlank()) text.add("TEL:" + contact.getPhone());
        if (!contact.getEmail().isBlank()) text.add("EMAIL;TYPE=E-Mail:" + contact.getEmail());
        text.add("END:VCARD");

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(text.toString().getBytes());
        } catch (IOException e) {
            return null;
        }
        return file;
    }
}
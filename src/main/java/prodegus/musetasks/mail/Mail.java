package prodegus.musetasks.mail;



import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.util.Properties;


public class Mail {

    public static void main(String[] args) {
        Message.RecipientType type = Message.RecipientType.TO;
        Properties properties = new Properties();
        properties.put("mail.smtp.auth",  "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp-mail.outlook.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.trust", "smtp-mail.outlook.com");

        String myAccount = "testnutzer59@outlook.de";
        String myPassword = "asdfasdf0!";
        String empfaenger = "daniel.hanelt@outlook.com";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccount, myPassword);
            }
        });

        try {
            System.out.println("Flag 1");
            Message message = prepareMessage(session, myAccount, empfaenger);
            System.out.println("Flag 2");
            Transport.send(message);
            System.out.println("E-Mail erfolgreich versendet!");
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    private static Message prepareMessage(Session session, String myAccount, String empfaenger) throws Exception{

        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(myAccount));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(empfaenger));
        message.setSubject("Mail Test");

        // Multipart-Message ("Wrapper") erstellen
        Multipart multipart = new MimeMultipart();
        // Body-Part setzen:
        BodyPart messageBodyPart = new MimeBodyPart();
        // Textteil des Body-Parts
        messageBodyPart.setText("Text-Inhalt der E-Mail zum Testen");
        // Body-Part dem Multipart-Wrapper hinzufügen
        multipart.addBodyPart(messageBodyPart);
        // Message fertigstellen, indem sie mit dem Multipart-Content ausgestattet wird
        message.setContent(multipart);

        return message;
    }


}


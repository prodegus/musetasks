package prodegus.musetasks.mail;
import java.util.Properties;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class JakartaEmail {
   public static void main(String[] args) {
      //provide recipient's email ID
      String to = "daniel.hanelt@gmail.com";
      //provide sender's email ID
      String from = "testnutzer59@outlook.com";
      //provide Mailtrap's username
      final String username = "testnutzer59@outlook.com";
      //provide Mailtrap's password
      final String password = "asdfasdf0!";
      //provide Mailtrap's host address
      String host = "smtp-mail.outlook.com";
      //configure Mailtrap's SMTP server details
      Properties props = new Properties();
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.smtp.host", host);
      props.put("mail.smtp.port", "587");
      props.put("mail.smtp.ssl.trust", host);
      //create the Session object
      Authenticator authenticator = new Authenticator() {
protected PasswordAuthentication getPasswordAuthentication() {
return new PasswordAuthentication(username, password);
}
};
Session session = Session.getInstance(props, authenticator);

      try {
    //create a MimeMessage object
    Message message = new MimeMessage(session);
    //set From email field
    message.setFrom(new InternetAddress(from));
    //set To email field
    message.setRecipients(Message.RecipientType.TO,
               InternetAddress.parse(to));
    //set email subject field
    message.setSubject("Here comes Jakarta Mail!");
    //set the content of the email message
    message.setText("Just discovered that Jakarta Mail is fun and easy to use");
    //send the email message
    Transport.send(message);
    System.out.println("Email Message Sent Successfully");
      } catch (MessagingException e) {
         throw new RuntimeException(e);
      }
   }
}
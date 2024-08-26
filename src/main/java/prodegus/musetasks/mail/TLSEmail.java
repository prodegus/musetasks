package prodegus.musetasks.mail;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import prodegus.musetasks.contacts.Contact;

import static prodegus.musetasks.contacts.VCard.vCard;
import static prodegus.musetasks.mail.EmailModel.*;
import static prodegus.musetasks.ui.StageFactories.APP_NAME;

public class TLSEmail {

	public static boolean confirmMail(String to) {
		LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
		String timestamp = formatter.format(now);

		String mailConfirmMessage = "Ihr E-Mail-Konto wurde erfolgreich mit " + APP_NAME + " verknüpft am " + timestamp + ".";
		return sendMail(to, "E-Mail-Konto wurde mit " + APP_NAME + " verknüpft!", mailConfirmMessage, APP_NAME);
	}

	public static boolean sendMail(String to, String subject, String messageText) {
		return sendMail(to, subject, messageText, getMailSender());
	}

	public static boolean sendMail(String to, String subject, String messageText, String sender) {
		return sendMail(to, "", subject, messageText, sender, null);
	}

	public static boolean sendMail(String to, String cc, String subject, String messageText, String sender, List<File> attachments) {
		Properties props = getPropertiesOutlookSMTP();
		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(getMailUser(), getMailPassword());
			}
		});

		try {
			MimeMessage message = new MimeMessage(session);
			BodyPart textBodyPart = new MimeBodyPart();
			Multipart multipart = new MimeMultipart();

			message.addHeader("Content-type", "text/HTML; charset=UTF-8");
			message.addHeader("format", "flowed");
			message.addHeader("Content-Transfer-Encoding", "8bit");
			message.setFrom(new InternetAddress(getMailUser(), sender));
			message.setReplyTo(InternetAddress.parse(getMailUser(), false));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
			if (!cc.isEmpty()) message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(cc, false));
			message.setSubject(subject, "UTF-8");
			message.setSentDate(new Date());

			textBodyPart.setText(messageText);
			multipart.addBodyPart(textBodyPart);

			if (attachments != null) {
				for (File attachment : attachments) {
					if (attachment != null) {
						DataSource source = new FileDataSource(attachment.getPath());
						BodyPart attachmentBodyPart = new MimeBodyPart();
						attachmentBodyPart.setDataHandler(new DataHandler(source));
						attachmentBodyPart.setFileName(attachment.getPath());
						multipart.addBodyPart(attachmentBodyPart);
					}
				}
			}

			message.setContent(multipart);

			Transport.send(message);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static Properties getPropertiesOutlookSMTP() {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp-mail.outlook.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");
		props.put("mail.smtp.ssl.trust", "smtp-mail.outlook.com");
		return props;
	}

}
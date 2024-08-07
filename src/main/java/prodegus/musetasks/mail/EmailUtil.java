package prodegus.musetasks.mail;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class EmailUtil {

	public static final String errorMail = "testnutzer59@outlook.com";

	public static String code =
			"package prodegus.musetasks.mail;\n" +
					"\n" +
					"import java.time.LocalDate;\n" +
					"import java.util.Date;\n" +
					"import java.util.Properties;\n" +
					"\n" +
					"import jakarta.mail.*;\n" +
					"import jakarta.mail.internet.InternetAddress;\n" +
					"import jakarta.mail.internet.MimeMessage;\n" +
					"\n" +
					"import static prodegus.musetasks.login.Settings.*;\n" +
					"import static prodegus.musetasks.mail.EmailUtil.code;\n" +
					"import static prodegus.musetasks.mail.EmailUtil.getPropertiesOutlookSMTP;\n" +
					"import static prodegus.musetasks.ui.StageFactories.APP_NAME;\n" +
					"\n" +
					"public class TLSEmail {\n" +
					"\n" +
					"\tpublic static void main(String[] args) {\n" +
					"\t\tsetMailCredentials(\"testnutzer59@outlook.com\", \"********\", \"Test-Musikschule\");\n" +
					"\n" +
					"\t\tsendMail(\"daniel.hanelt@gmail.com\", \"Userdaten aus Prefs gelesen\",\n" +
					"\t\t\t\t\"Empfänger, Betreff und Nachricht wurden beim Aufruf übergeben!\\n\\n\" +\n" +
					"\t\t\t\t\t\t\"Folgender Code:\\n\\n\" +\n" +
					"\t\t\t\t\t\t\"\" +\n" +
					"\t\t\t\t\t\tcode);\n" +
					"\t}\n" +
					"\t\n" +
					"\tpublic static boolean confirmMail(String recipient) {\n" +
					"\t\tString mailConfirmMessage = \"Ihr E-Mail-Konto wurde erfolgreich mit \" + APP_NAME + \"verknüpft am \" + LocalDate.now() + \".\";\n" +
					"\t\treturn sendMail(recipient, \"E-Mail-Konto wurde mit \" + APP_NAME + \"verknüpft!\", mailConfirmMessage, APP_NAME);\n" +
					"\t}\n" +
					"\n" +
					"\tpublic static boolean sendMail(String recipients, String subject, String message) {\n" +
					"\t\treturn sendMail(recipients, subject, message, getMailSender());\n" +
					"\t}\n" +
					"\t\n" +
					"\tpublic static boolean sendMail(String recipients, String subject, String message, String sender) {\n" +
					"\t\tSystem.out.println(\"sendMail() invoked\");\n" +
					"\n" +
					"\t\tProperties props = getPropertiesOutlookSMTP();\n" +
					"\n" +
					"\t\tSession session = Session.getInstance(props);\n" +
					"\n" +
					"\t\ttry {\n" +
					"\t\t\tMimeMessage msg = new MimeMessage(session);\n" +
					"\t\t\tmsg.addHeader(\"Content-type\", \"text/HTML; charset=UTF-8\");\n" +
					"\t\t\tmsg.addHeader(\"format\", \"flowed\");\n" +
					"\t\t\tmsg.addHeader(\"Content-Transfer-Encoding\", \"8bit\");\n" +
					"\t\t\tmsg.setFrom(new InternetAddress(getMailUser(), sender));\n" +
					"\t\t\tmsg.setReplyTo(InternetAddress.parse(getMailUser(), false));\n" +
					"\t\t\tmsg.setSubject(subject, \"UTF-8\");\n" +
					"\t\t\tmsg.setText(message, \"UTF-8\");\n" +
					"\t\t\tmsg.setSentDate(new Date());\n" +
					"\t\t\tmsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients, false));\n" +
					"\n" +
					"\t\t\tSystem.out.println(\"Message is ready\");\n" +
					"\n" +
					"\t\t\tTransport.send(msg, getMailUser(), getMailPassword());\n" +
					"\n" +
					"\t\t\tSystem.out.println(\"Email sent successfully!!\");\n" +
					"\t\t}\n" +
					"\t\tcatch (Exception e) {\n" +
					"\t\t\te.printStackTrace();\n" +
					"\t\t\treturn false;\n" +
					"\t\t}\n" +
					"\t\treturn true;\n" +
					"\t}\n" +
					"\n" +
					"}";

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
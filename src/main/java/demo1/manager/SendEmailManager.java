package demo1.manager;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import demo1.utility.ApplicationProperties;

public class SendEmailManager {
	
	private ApplicationProperties appProp;
	private Properties properties;
	
	public SendEmailManager() {
		this.appProp = ApplicationProperties.getInstance();
		
		this.properties = System.getProperties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.host", "smtp.gmail.com");
	}
	
	public void send( String subjet, String email, String message ) throws MessagingException { 
		Session messageSession = Session.getInstance(properties, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				try {
					return new PasswordAuthentication(appProp.getUserMail(), appProp.getPswMail());
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
		});
		
		MimeMessage mimeMessage = new MimeMessage(messageSession);
		mimeMessage.setSubject(subjet);
		mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
		
		mimeMessage.setContent(message, "text/html; charset=utf-8");
		Transport.send(mimeMessage);
	}
	


}

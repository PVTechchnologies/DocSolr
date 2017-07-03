package com.docsolr.service.common;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Rajkiran
 *
 */
@Component
public class MakeMessage {
	private @Value("#{mailProperties['MAIL_SENDER']}") String sender;
	private @Value("#{mailProperties['MAIL_PASSWORD']}") String password;
	private @Value("#{mailProperties['HOST_NAME_MAIL_TEMPLATE']}") String hostname;
	private @Value("#{mailProperties['MAIL_HOST_NAME']}") String mailhostname;
	private @Value("#{mailProperties['MAIL_SOCKET_PORT']}") String mailSocketPort;
	private @Value("#{mailProperties['MAIL_SOCKET_CLASS']}") String mailSocketClass;
	private @Value("#{mailProperties['MAIL_SMTP_AUTH']}") String mailSmtpAuth;
	private @Value("#{mailProperties['MAIL_SMTP_PORT']}") String mailSmtpPort;
	static Logger logger = Logger.getLogger(MakeMessage.class);
	
	public static final String MODULE = "[MakeMessage] # ";
	private Session session = null;
	private Message message;
	private Multipart multipart;
	private StringBuffer stringBuffer;
	public String smtpServer;
	//private String sender;
	private String[] reciver;
	private String[] ccaddress;
	private String[] bcaddress;
	private String subject;
	private String content;
	private String[] paths;
	private Properties properties;

	private void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}

	public String getSmtpServer() {
		return this.smtpServer;
	}

	private void setSender(String sender) {
		this.sender = sender;
	}

	public String getSender() {
		return this.sender;
	}

	private void setCcaddress(String[] ccaddress) {
		this.ccaddress = ccaddress;
	}

	private void setBcaddress(String[] bcaddress) {
		this.bcaddress = bcaddress;
	}

	private void setReciver(String[] reciver) {
		this.reciver = reciver;
	}

	public String[] getReciver() {
		return this.reciver;
	}

	private void setSubject(String subject) {
		this.subject = subject;
	}

	private void setContent(String content) {
		this.content = content;
	}

	private void setAttachPaths(String[] paths) {
		this.paths = paths;
	}

	public void connectionSetting(boolean isDebugging) {
		String smtpServer = mailhostname;		
		setSmtpServer(smtpServer);
		properties = new Properties();
		properties.put("mail.smtp.host", smtpServer);
		properties.put("mail.smtp.socketFactory.port", mailSocketPort);
		properties.put("mail.smtp.socketFactory.class",mailSocketClass);
		properties.put("mail.smtp.auth", mailSmtpAuth);
		properties.put("mail.smtp.port", mailSmtpPort);
		session = Session.getDefaultInstance(properties,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(sender, password);
					}
				});
		session.setDebug(isDebugging);
	}


	public boolean send(String sender, String[] reciver, String[] ccaddress, String[] bccaddress, String subject, String content,
			String[] paths) {
		boolean eSent = false ;
		setSender(sender);
		setReciver(reciver);
		setSubject(subject);
		setContent(content);
		setAttachPaths(paths);
		setCcaddress(ccaddress);
		setBcaddress(bccaddress);

		try {
			this.message = new MimeMessage(session);
			InternetAddress from = null;
			from = new InternetAddress(this.sender);
			this.message.setFrom(from);
			InternetAddress[] address = null;
			InternetAddress[] ccadd = null;
			InternetAddress[] bcadd = null;
			if (this.reciver != null) {
				address = new InternetAddress[this.reciver.length];

				for (int i = 0; i < this.reciver.length; i++) {
						address[i] = new InternetAddress(this.reciver[i]);
				}
				this.message.setRecipients(Message.RecipientType.TO, address);
			}
			if (this.ccaddress != null) {

				ccadd = new InternetAddress[this.ccaddress.length];
				for (int i = 0; i < this.ccaddress.length; i++) {
					ccadd[i] = new InternetAddress(this.ccaddress[i]);
				}
				this.message.setRecipients(Message.RecipientType.CC, ccadd);
			}

			if (this.bcaddress != null) {

				bcadd = new InternetAddress[this.bcaddress.length];
				for (int i = 0; i < this.bcaddress.length; i++) {
					bcadd[i] = new InternetAddress(this.bcaddress[i]);
				}
				this.message.setRecipients(Message.RecipientType.BCC, bcadd);
			}

			this.message.setSubject(this.subject);
			this.stringBuffer = new StringBuffer();
			this.stringBuffer.append(this.content);
			MimeBodyPart mbp1 = new MimeBodyPart();
			mbp1.setContent(new String(this.stringBuffer), "text/html");
			this.multipart = new MimeMultipart();
			this.multipart.addBodyPart(mbp1);

			if (this.paths != null) {
				MimeBodyPart mbp2 = null;
				File file = null;
				FileDataSource fds = null;

				for (int i = 0; i < this.paths.length; i++) {
					mbp2 = new MimeBodyPart();
					String filename = this.paths[i];
					file = new File(this.paths[i]);

					if (file.exists()) {
						fds = new FileDataSource(file);
						mbp2.setDataHandler(new DataHandler(fds));
						mbp2.setFileName(fds.getName());
						this.multipart.addBodyPart(mbp2);
					}
				}
				file = null;
			}

			try {
				this.message.setContent(this.multipart);
				this.message.setSentDate(new Date());
				Transport.send(this.message);
				eSent = true ;
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(MODULE+"Exception Occured : " + ex, ex);
			}
		} catch (MessagingException mex) {
			mex.printStackTrace(System.err);
			logger.error(MODULE+" Message Exception Occured : " + mex, mex);
		} catch (Exception ex) {
			logger.error(MODULE+"Exception Occured : " + ex, ex);
		}
		return eSent;
	}

}

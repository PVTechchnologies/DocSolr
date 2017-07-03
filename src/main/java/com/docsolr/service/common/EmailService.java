package com.docsolr.service.common;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.docsolr.entity.Users;


/**
 * 
 * @author Rajkiran
 *
 */
@Service
public class EmailService {

	static Logger logger = Logger.getLogger(EmailService.class);
	public static final String MODULE = "[EmailService] # ";
	public static final long serialVersionUID = 123345300;
	private @Value("#{mailProperties['MAIL_TEMPLATE_PATH']}") String mailTemplatePath;
	private @Value("#{mailProperties['MAIL_SENDER']}") String mailSender;
	private @Value("#{mailProperties['RESET_PASSWORD_MAIL_SUBJECT']}") String resetPassSubject;
	private @Value("#{mailProperties['RESET_PASSWORD_MAIL_TEMPLATE']}") String resetPasswordMailTemplate;
	private @Value("#{mailProperties['HOST_NAME_MAIL_TEMPLATE']}") String mailTemplateHostName;
	private @Value("#{mailProperties['MAIL_RECEPIENT_BCC']}") String mailRecepientBcc;
	
	
	@Autowired
	MakeMessage makeMessage;
	
	/**
	 * @author Rajkiran
	 * @param username
	 * @param strpassword
	 * @param emailId
	 * @param strTo
	 * @param url
	 * @return
	 */
	public boolean sendMail(String username,String strpassword,String emailId,String strTo,String url) {
		String METHOD = "sendMail(String strloginname,String strpassword,String strName,String strTo)  -> ";		
		boolean eSent = false;		
		String strSender;	
		String templateAddress;		
		String strSubject;
		String mailTemplate;
		String strTemplatename="";
		String mailContent;
		String resetpasswordlink = url;
		try {
			
			templateAddress = mailTemplatePath;
			strSender = mailSender;
			strSubject =  resetPassSubject;
			strTemplatename = resetPasswordMailTemplate;
			mailTemplate = HtmlTemplate.getHtmlFileData(templateAddress+strTemplatename);	
			makeMessage.connectionSetting(false);	
			username = username != null ? username : emailId;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			Integer year = calendar.get(Calendar.YEAR);
			mailContent = mailTemplate.replace("$username$",username);
			mailContent = mailContent.replace("$loginname$",emailId);
			mailContent = mailContent.replace("$year$",year.toString());
			mailContent = mailContent.replace("$resetpasswordlink$","<a href='"+resetpasswordlink+"'>Click Here </a>to set your password");
			mailContent = mailContent.replace("$hostname$",mailTemplateHostName);
			logger.debug(MODULE + METHOD + "  Sending Mail To : "+strTo +" $participantname$ ::"+emailId);
			strTo = emailId;
			String[] recipientList = {strTo};	
			String[] bccList = null; 
			eSent = makeMessage.send(strSender, recipientList,null,bccList,strSubject, mailContent, null);
				
		} catch (Exception e) {
			logger.error(MODULE + METHOD + " Exception occured " + e, e);			
		}
		return eSent;
	}	
	
	/**
	 * @author Rajkiran
	 * @param user
	 * @param strTemplatename
	 * @param strSubject
	 * @param hyperlink
	 * @param type
	 * @return
	 */
	public boolean sendMail(Users user,String strTemplatename, String strSubject, String[] hyperlink,String type) {
		String METHOD = "sendMail(UserEntity user,String strTemplatename, String strSubject, String[] hyperlink,String type)  -> ";		
		boolean eSent = false;		
		String strSender;	
		String templateAddress;		
		String mailTemplate;
		String mailContent;
		String username;
		String strTo = user.getEmail();
		try {
			
			templateAddress = mailTemplatePath;
			strSender = mailSender;
			mailTemplate = HtmlTemplate.getHtmlFileData(templateAddress+strTemplatename);	
			makeMessage.connectionSetting(false);	
			username = user != null ? user.getFirstName() : user.getEmail();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			Integer year = calendar.get(Calendar.YEAR);
			mailContent = mailTemplate.replace("$username$",username);
			mailContent = mailContent.replace("$year$",year.toString());
			if("Rating".equalsIgnoreCase(type)){
				mailContent = mailContent.replace("$challengesubmissionlink$","<a href='"+mailTemplateHostName+hyperlink[0]+"'>Click Here </a>");
				mailContent = mailContent.replace("$loginname$",strTo);
			}else if("EmailVerification".equalsIgnoreCase(type)){
				mailContent = mailContent.replace("$emailverificationlink$","<a href='"+mailTemplateHostName+hyperlink[0]+"'>Click Here </a>");
				mailContent = mailContent.replace("$loginname$",strTo);
			}
			mailContent = mailContent.replace("$hostname$",mailTemplateHostName);
			String[] recipientList = {strTo};	
			String[] bccList = null; 
			eSent = makeMessage.send(strSender, recipientList,null,null,strSubject, mailContent, null);
				
		} catch (Exception e) {
			logger.error(MODULE + METHOD + " Exception occured " + e, e);			
		}
		return eSent;
	}

}

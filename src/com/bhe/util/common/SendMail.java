package com.bhe.util.common;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

 

public class SendMail {
	
	public static String getBaseUrl(HttpServletRequest request) {
		//return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
		return request.getScheme()+"://"+request.getServerName()+request.getContextPath()+"/";
		}
	
	
	public static void send(String mailbox, String title, String content) throws Exception {
		exec(mailbox, title, content);
	}
	/**
	 * 发送邀请邮件
	 * @param mailbox
	 * @param title
	 * @param content
	 * @throws Exception
	 */
	public static void invite(String mailbox, String title, String content) throws Exception{
		
		exec(mailbox, title, content);
		
	}
	/*public static String getContext(String key){
		return "尊敬的用户您好：感谢你注册百居易电子商务平台，您的肯定就是我们前进的动力！<br/>恭喜您找回密码成功,你的密码是: "+key+"<br/></a>";
	}*/
	
	public static void exec(String mailbox, String title, String content)throws Exception{
		
		String mail_from = MailBean.mailAddress; // mailbox 发送到哪 title 标题
		Properties props = new Properties();
		
		props.put("mail.smtp.host", MailBean.mailServer);
		
		props.put("mail.smtp.auth", "true");
		
		Session s = Session.getInstance(props);
		
		s.setDebug(true);
		
		MimeMessage message = new MimeMessage(s);
		
		InternetAddress from = new InternetAddress(mail_from);
		
		message.setFrom(from);
		
		InternetAddress to = new InternetAddress(mailbox);
		
		message.setRecipient(Message.RecipientType.TO, to);
		
		message.setSubject(title);
		
		message.setText(content);
		
		message.setContent(content, "text/html;charset=gbk");
		
		message.setSentDate(new Date());
		
		message.saveChanges();
		
		Transport transport = s.getTransport("smtp");
		
		transport.connect(MailBean.mailServer, MailBean.mailCount, MailBean.mailPassword);
		
		transport.sendMessage(message, message.getAllRecipients());
		
		transport.close();
		
	}
	
	
	public static void main(String args[]) throws Exception{
send("245484419@qq.com", "百居易电子商务平台密码提醒", "填写邮件内容" );
	}

}

package com.mdtech.smartcontactmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendMailService {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	public void SendMail(String reciever, String subject, String msgBody) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(reciever);
		msg.setSubject(subject);
		msg.setText(msgBody);
		
		javaMailSender.send(msg);
	}

}

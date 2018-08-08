package com.bridgelabz.todo.user.services;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.bridgelabz.todo.user.models.Email;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender emailSender;
	
	@Value("${mode}")
	private String mode;

	@Override
	public void sendEmail(Email email) throws MessagingException {
		MimeMessage message = emailSender.createMimeMessage();
	      
	    MimeMessageHelper helper = new MimeMessageHelper(message, true);
	     
	    if(mode.equals("development")) {
	    	email.setTo("ghargesiddharth@gmail.com");
	    }
	    
	    helper.setTo(email.getTo());
	    helper.setSubject(email.getSubject());
	    helper.setText(email.getText(), true);
	 
	    emailSender.send(message);
	}
}

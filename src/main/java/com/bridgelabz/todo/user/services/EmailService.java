package com.bridgelabz.todo.user.services;

import javax.mail.MessagingException;

import com.bridgelabz.todo.user.models.Email;

public interface EmailService {
	
	void sendEmail(Email email) throws MessagingException;

}

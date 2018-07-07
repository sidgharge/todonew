package com.bridgelabz.todo.user.services;

import java.io.IOException;

import javax.mail.MessagingException;

import com.bridgelabz.todo.user.exceptions.RegistrationException;
import com.bridgelabz.todo.user.exceptions.UserActivationException;
import com.bridgelabz.todo.user.models.RegistrationDto;

public interface UserService {

	void register(RegistrationDto registrationDto, String url) throws RegistrationException, MessagingException, IOException;

	void activateUser(String token) throws UserActivationException;
}

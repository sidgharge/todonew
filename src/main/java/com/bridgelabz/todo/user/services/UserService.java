package com.bridgelabz.todo.user.services;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.mail.MessagingException;

import com.bridgelabz.todo.user.exceptions.RegistrationException;
import com.bridgelabz.todo.user.models.RegistrationDto;

public interface UserService {

	void register(RegistrationDto registrationDto) throws RegistrationException, MessagingException, IOException;
}

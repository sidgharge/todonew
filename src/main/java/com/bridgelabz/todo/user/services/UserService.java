package com.bridgelabz.todo.user.services;

import java.io.IOException;

import javax.mail.MessagingException;

import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.todo.user.exceptions.RegistrationException;
import com.bridgelabz.todo.user.exceptions.UserActivationException;
import com.bridgelabz.todo.user.models.RegistrationDto;
import com.bridgelabz.todo.user.models.UserDto;

public interface UserService {

	void register(RegistrationDto registrationDto, String url) throws RegistrationException, MessagingException, IOException;

	void activateUser(String token) throws UserActivationException;

	String uploadProfilePicture(MultipartFile image, String url, long userId);

	UserDto getUserProfile(long id);
}

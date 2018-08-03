package com.bridgelabz.todo.user.services;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.mail.MessagingException;

import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.todo.user.exceptions.EmailAlreadyRegisteredException;
import com.bridgelabz.todo.user.exceptions.InvalidPasswordException;
import com.bridgelabz.todo.user.exceptions.RegistrationException;
import com.bridgelabz.todo.user.exceptions.TokenMalformedException;
import com.bridgelabz.todo.user.exceptions.UserActivationException;
import com.bridgelabz.todo.user.exceptions.UserNotFoundException;
import com.bridgelabz.todo.user.models.RegistrationDto;
import com.bridgelabz.todo.user.models.ResetPasswordDto;
import com.bridgelabz.todo.user.models.UserDto;

public interface UserService {

	void register(RegistrationDto registrationDto, String url) throws RegistrationException, MessagingException, IOException, EmailAlreadyRegisteredException;

	void activateUser(String token) throws UserActivationException;

	String uploadProfilePicture(MultipartFile image, String url, long userId);

	UserDto getUserProfile(long id);

	UserDto getUserProfile(String email) throws UserNotFoundException;

	void sendResetLink(String email, String url) throws UserNotFoundException, MessagingException, IOException;

	void resetPassword(String token, ResetPasswordDto resetPassword) throws TokenMalformedException, InvalidPasswordException;
}

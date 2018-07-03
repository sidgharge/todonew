package com.bridgelabz.todo.user.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.token.Token;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.bridgelabz.todo.user.exceptions.RegistrationException;
import com.bridgelabz.todo.user.factories.UserFactory;
import com.bridgelabz.todo.user.models.Email;
import com.bridgelabz.todo.user.models.RegistrationDto;
import com.bridgelabz.todo.user.models.User;
import com.bridgelabz.todo.user.repositories.UserRepository;
import com.bridgelabz.todo.user.utils.UserUtility;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserFactory userFactory;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private EmailService emailService;
	
	@Value("${registration.template.path}")
	private String emailTemplatePath;

	@Override
	public void register(RegistrationDto registrationDto) throws RegistrationException, MessagingException, IOException {
		UserUtility.validateUser(registrationDto);

		User user = userFactory.getUserFromRegistrationDto(registrationDto);
		user.setActivated(false);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole("USER");

		userRepository.save(user);
		
		File mailFile = ResourceUtils.getFile(emailTemplatePath);
		String mailText = new String(Files.readAllBytes(mailFile.toPath()));
		mailText = mailText.replace("@link", "http://localhost:8070/activate");
		
		Email email = userFactory.getEmail("ghargesiddharth@gmail.com", "User activation", mailText);
		emailService.sendSimpleMessage(email);		
	}
	
	

}

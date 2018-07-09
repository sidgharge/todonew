package com.bridgelabz.todo.user.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.bridgelabz.todo.user.exceptions.RegistrationException;
import com.bridgelabz.todo.user.exceptions.UserActivationException;
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
	public void register(RegistrationDto registrationDto, String url) throws IOException, MessagingException {
		UserUtility.validateUser(registrationDto);

		User user = userFactory.getUserFromRegistrationDto(registrationDto);
		user.setActivated(false);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole("USER");

		userRepository.save(user);

		File mailFile = ResourceUtils.getFile(emailTemplatePath);
		String mailText = new String(Files.readAllBytes(mailFile.toPath()));

		String token = UserUtility.generate(user.getId(), -1, "activation_token");
		mailText = mailText.replace("@link", url + "/activate?token=" + token);

		Email email = userFactory.getEmail("ghargesiddharth@gmail.com", "User activation", mailText);
		emailService.sendEmail(email);
	}

	@Override
	public void activateUser(String token) throws UserActivationException {
		try {
			long userId = UserUtility.verify(token);

			Optional<User> optionalUser = userRepository.findById(userId);

			if (!optionalUser.isPresent()) {
				throw new UserActivationException("User does not exist");
			}

			User user = optionalUser.get();
			user.setActivated(true);

			userRepository.save(user);
		} catch (Exception e) {
			throw new UserActivationException("Malformed link");
		}

	}

}

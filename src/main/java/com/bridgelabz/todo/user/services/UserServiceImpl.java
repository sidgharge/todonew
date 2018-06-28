package com.bridgelabz.todo.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.todo.user.exceptions.RegistrationException;
import com.bridgelabz.todo.user.factories.UserFactory;
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

	@Override
	public void register(RegistrationDto registrationDto) throws RegistrationException {
		UserUtility.validateUser(registrationDto);

		User user = userFactory.getUserFromRegistrationDto(registrationDto);
		user.setActivated(false);

		userRepository.save(user);
	}

}

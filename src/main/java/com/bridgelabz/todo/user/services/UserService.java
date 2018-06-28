package com.bridgelabz.todo.user.services;

import com.bridgelabz.todo.user.exceptions.RegistrationException;
import com.bridgelabz.todo.user.models.RegistrationDto;

public interface UserService {

	void register(RegistrationDto registrationDto) throws RegistrationException;
}

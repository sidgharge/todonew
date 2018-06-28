package com.bridgelabz.todo.user.utils;

import com.bridgelabz.todo.user.exceptions.RegistrationException;
import com.bridgelabz.todo.user.models.RegistrationDto;

public class UserUtility {
	
	private static final String CONTACT_PATTERN = "^[0-9]{10}$";

	public static void validateUser(RegistrationDto registrationDto) throws RegistrationException {
		if (registrationDto.getFirstname() == null || registrationDto.getFirstname().length() < 3) {
			throw new RegistrationException("Firstname should have at least 3 characters");
		}
		if (registrationDto.getLastname() == null || registrationDto.getLastname().length() < 3) {
			throw new RegistrationException("Lastname should have at least 3 characters");
		}
		if (registrationDto.getContact() == null || !registrationDto.getContact().matches(CONTACT_PATTERN)) {
			throw new RegistrationException("Contact number should have exactly 10 digits");
		}
		if (registrationDto.getPassword() == null || registrationDto.getPassword().length() < 8) {
			throw new RegistrationException("Password should be at least 8 characters long");
		}
		if (registrationDto.getConfirmPassword() == null || !registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
			throw new RegistrationException("Password and confirm password should match");
		}
	}
}

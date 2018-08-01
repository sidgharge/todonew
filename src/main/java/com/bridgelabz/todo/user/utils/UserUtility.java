package com.bridgelabz.todo.user.utils;

import com.bridgelabz.todo.user.exceptions.InvalidPasswordException;
import com.bridgelabz.todo.user.exceptions.RegistrationException;
import com.bridgelabz.todo.user.models.RegistrationDto;
import com.bridgelabz.todo.user.models.ResetPasswordDto;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;

public class UserUtility {

	private static final String CONTACT_PATTERN = "^[0-9]{10}$";

	private static final String KEY = "todoapp";

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
		if (registrationDto.getConfirmPassword() == null
				|| !registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
			throw new RegistrationException("Password and confirm password should match");
		}
	}
	
	public static void validateResetPasswordDto(ResetPasswordDto resetPasswordDto) throws InvalidPasswordException {
		if(resetPasswordDto.getPassword() == null || !resetPasswordDto.getPassword().equals(resetPasswordDto.getConfirmPassword())) {
			throw new InvalidPasswordException("Both the passwords do not match");
		}
		
		if (resetPasswordDto.getPassword().length() < 8) {
			throw new InvalidPasswordException("Password should have at least 8 characters");
		}
	}

	public static String generate(Object userId, long tokenLife, String subject) {

		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		JwtBuilder builder = Jwts.builder();

		builder.setSubject(subject);

		Date issuedAt = new Date();
		builder.setIssuedAt(issuedAt);

		if (tokenLife > 0) {
			Date expiresAt = new Date(issuedAt.getTime() + tokenLife);
			builder.setExpiration(expiresAt);
		}

		builder.setIssuer(String.valueOf(userId));

		builder.signWith(signatureAlgorithm, KEY);

		String compactJwt = builder.compact();

		return compactJwt;
	}

	public static long verify(String token) {
		JwtParser parser = Jwts.parser();
		try {
			Claims claims = parser.setSigningKey(KEY).parseClaimsJws(token).getBody();
			return Long.parseLong(claims.getIssuer());
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static String getRequestUrl(HttpServletRequest request) throws MalformedURLException {
		URL url = new URL(request.getRequestURL().toString());
		return url.getProtocol() + "://" + url.getHost() + ":" + url.getPort() + request.getContextPath();
	}

}

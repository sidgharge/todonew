package com.bridgelabz.todo.user.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.Principal;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.todo.note.utils.NotesUtility;
import com.bridgelabz.todo.user.exceptions.EmailAlreadyRegisteredException;
import com.bridgelabz.todo.user.exceptions.InvalidPasswordException;
import com.bridgelabz.todo.user.exceptions.RegistrationException;
import com.bridgelabz.todo.user.exceptions.TokenMalformedException;
import com.bridgelabz.todo.user.exceptions.UserActivationException;
import com.bridgelabz.todo.user.exceptions.UserNotFoundException;
import com.bridgelabz.todo.user.models.RegistrationDto;
import com.bridgelabz.todo.user.models.ResetPasswordDto;
import com.bridgelabz.todo.user.models.Response;
import com.bridgelabz.todo.user.models.UserDto;
import com.bridgelabz.todo.user.services.UserService;
import com.bridgelabz.todo.user.utils.UserUtility;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private WebApplicationContext context;

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegistrationDto registrationDto, HttpServletRequest request)
			throws MessagingException, IOException, RegistrationException, EmailAlreadyRegisteredException {

		String url = UserUtility.getRequestUrl(request);
		userService.register(registrationDto, url);

		Response response = context.getBean(Response.class);
		response.setMessage("Succesfully registered");
		response.setStatus(1);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/activate")
	public ResponseEntity<?> activateUser(@RequestParam("token") String token) throws UserActivationException {
		userService.activateUser(token);

		Response response = context.getBean(Response.class);
		response.setMessage("Email id successfully verified");
		response.setStatus(1);

		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	@PostMapping("/user/upload-image")
	public ResponseEntity<Response> uploadImage(HttpServletRequest request, @RequestParam("image") MultipartFile image,
			Principal principal) throws MalformedURLException {
		String link = userService.uploadProfilePicture(image, NotesUtility.getUrl(request, ""),
				Long.parseLong(principal.getName()));

		Response response = context.getBean(Response.class);
		response.setMessage(link);
		response.setStatus(1);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/user/profile")
	public ResponseEntity<UserDto> getUserProfile(Principal principal) {
		UserDto userDto = userService.getUserProfile(Long.parseLong(principal.getName()));

		return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
	}

	@GetMapping("/user/profilebyemail")
	public ResponseEntity<UserDto> getUserProfile(@RequestParam("email") String email) throws UserNotFoundException {
		UserDto userDto = userService.getUserProfile(email);

		return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
	}

	@PostMapping("/send-reset-link")
	public ResponseEntity<Response> sendResetLink(@RequestParam("email") String email,
			@RequestHeader("origin") String url) throws UserNotFoundException, MessagingException, IOException {
		userService.sendResetLink(email, url);

		Response response = context.getBean(Response.class);
		response.setMessage("Activation link sent succefully");
		response.setStatus(1);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/reset-password")
	public ResponseEntity<Response> resetPassword(@RequestHeader("token") String token,
			@RequestBody ResetPasswordDto resetPassword) throws TokenMalformedException, InvalidPasswordException {
		userService.resetPassword(token, resetPassword);

		Response response = context.getBean(Response.class);
		response.setMessage("Password reseted succefully");
		response.setStatus(1);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("demo")
	public ResponseEntity<Response> demo() {
		Response response = context.getBean(Response.class);
		response.setMessage("Yo");
		response.setStatus(1);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}

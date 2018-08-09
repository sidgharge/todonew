package com.bridgelabz.todo.note.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.bridgelabz.todo.note.models.Note;
import com.bridgelabz.todo.user.factories.UserFactory;
import com.bridgelabz.todo.user.models.Email;
import com.bridgelabz.todo.user.models.User;
import com.bridgelabz.todo.user.services.EmailService;

@Service
public class AsyncService {
	
	@Value("${collaborate.template.path}")
	private String collaborateTemplatePath;
	
	@Autowired
	private UserFactory userFactory;
	
	@Autowired
	private EmailService emailService;

	@Async
	public void sendEmailToCollaborator(User owner, Note note, String origin, String emailId)
			throws IOException, MessagingException {
		File mailFile = ResourceUtils.getFile(collaborateTemplatePath);
		String mailText = new String(Files.readAllBytes(mailFile.toPath()));
		mailText = mailText.replace("{{firstname}}", owner.getFirstname());
		mailText = mailText.replace("{{email}}", owner.getEmail());
		mailText = mailText.replace("{{note.title}}", note.getTitle());

		origin = origin + "?share=" + note.getId();
		mailText = mailText.replace("{{origin}}", origin);

		Email email = userFactory.getEmail(emailId,
				owner.getFirstname() + " shared a note with you - " + note.getTitle(), mailText);
		emailService.sendEmail(email);
	}
}

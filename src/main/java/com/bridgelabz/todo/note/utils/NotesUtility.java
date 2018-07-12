package com.bridgelabz.todo.note.utils;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import com.bridgelabz.todo.note.exceptions.EmptyNoteException;
import com.bridgelabz.todo.note.models.CreateNoteDto;

public class NotesUtility {

	public static void validateNote(CreateNoteDto noteDto) {
		if (noteDto.getTitle() == null || noteDto.getTitle().isEmpty()) {
			if (noteDto.getBody() == null || noteDto.getBody().isEmpty()) {
				if (noteDto.getImageUrl() == null || noteDto.getImageUrl().isEmpty()) {
					throw new EmptyNoteException("Cannot create empty note");
				}
			}
		}
	}
	
	public static String getUrl(HttpServletRequest request, String suffix) throws MalformedURLException{
		URL url = new URL(request.getRequestURL().toString());
		String redirectUrl = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort() + request.getContextPath() + "/" + suffix;
		return redirectUrl;
	}
}

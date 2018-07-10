package com.bridgelabz.todo.note.models;

public class CreateNoteDto {

	private String title;

	private String body;

	private String imageUrl;

	private CreateNoteExtrasDto noteExtras;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public CreateNoteExtrasDto getNoteExtras() {
		return noteExtras;
	}

	public void setNoteExtras(CreateNoteExtrasDto noteExtras) {
		this.noteExtras = noteExtras;
	}

}

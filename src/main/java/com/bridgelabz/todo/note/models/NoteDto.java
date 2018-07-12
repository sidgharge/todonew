package com.bridgelabz.todo.note.models;

import java.util.Date;

public class NoteDto {

	private long id;

	private String title;

	private String body;

	private Date createdAt;

	private String imageUrl;

	private Date updatedAt;

	private NoteExtrasDto noteExtras;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public NoteExtrasDto getNoteExtras() {
		return noteExtras;
	}

	public void setNoteExtras(NoteExtrasDto noteExtras) {
		this.noteExtras = noteExtras;
	}

}

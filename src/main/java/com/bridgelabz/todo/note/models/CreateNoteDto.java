package com.bridgelabz.todo.note.models;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class CreateNoteDto {

	private String title;

	private String body;

	private List<String> imageUrls = new LinkedList<>();

	private boolean isPinned;

	private boolean isArchived;

	private boolean isTrashed;

	private String color;

	private Date reminder;

	private List<Long> labels = new LinkedList<>();

	private List<Long> collaborators = new LinkedList<>();

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

	public List<String> getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(List<String> imageUrls) {
		this.imageUrls = imageUrls;
	}

	public boolean isPinned() {
		return isPinned;
	}

	public void setPinned(boolean isPinned) {
		this.isPinned = isPinned;
	}

	public boolean isArchived() {
		return isArchived;
	}

	public void setArchived(boolean isArchived) {
		this.isArchived = isArchived;
	}

	public boolean isTrashed() {
		return isTrashed;
	}

	public void setTrashed(boolean isTrashed) {
		this.isTrashed = isTrashed;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Date getReminder() {
		return reminder;
	}

	public void setReminder(Date reminder) {
		this.reminder = reminder;
	}

	public List<Long> getLabels() {
		return labels;
	}

	public void setLabels(List<Long> labels) {
		this.labels = labels;
	}

	public List<Long> getCollaborators() {
		return collaborators;
	}

	public void setCollaborators(List<Long> collaborators) {
		this.collaborators = collaborators;
	}

}

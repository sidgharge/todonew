package com.bridgelabz.todo.note.models;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.bridgelabz.todo.user.models.UserDto;

public class NoteDto {

	private long id;

	private String title;

	private String body;

	private Date createdAt;

	private List<String> imageUrls = new LinkedList<>();

	private Date updatedAt;

	private boolean isPinned;

	private boolean isArchived;

	private boolean isTrashed;

	private String color;

	private Date reminder;

	private List<LabelDto> labels = new LinkedList<>();
	
	private UserDto owner;
	
	private List<UserDto> collaborators = new LinkedList<>();

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

	public List<LabelDto> getLabels() {
		return labels;
	}

	public void setLabels(List<LabelDto> labels) {
		this.labels = labels;
	}

	public UserDto getOwner() {
		return owner;
	}

	public void setOwner(UserDto owner) {
		this.owner = owner;
	}

	public List<UserDto> getCollaborators() {
		return collaborators;
	}

	public void setCollaborators(List<UserDto> collaborators) {
		this.collaborators = collaborators;
	}

}

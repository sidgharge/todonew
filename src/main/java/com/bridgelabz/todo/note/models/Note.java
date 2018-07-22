package com.bridgelabz.todo.note.models;

import java.util.Date;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.bridgelabz.todo.user.models.User;

@Entity
@Table
public class Note {

	@Id
	@GeneratedValue(generator = "noteidgenerator", strategy = GenerationType.AUTO)
	@GenericGenerator(name = "noteidgenerator", strategy = "native")
	private long id;

	private String title;

	private String body;

	private Date createdAt;

	private Date updatedAt;

	@ElementCollection
	private List<String> imageUrls;

	@ManyToOne
	@JoinColumn(name = "ownerId")
	private User owner;

	@OneToMany(mappedBy = "note")
	private List<NoteExtras> noteExtras;

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

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public List<String> getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(List<String> imageUrls) {
		this.imageUrls = imageUrls;
	}

	public List<NoteExtras> getNoteExtras() {
		return noteExtras;
	}

	public void setNoteExtras(List<NoteExtras> noteExtras) {
		this.noteExtras = noteExtras;
	}

}

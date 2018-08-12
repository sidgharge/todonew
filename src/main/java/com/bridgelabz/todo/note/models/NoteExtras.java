package com.bridgelabz.todo.note.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
//
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.JoinTable;
//import javax.persistence.ManyToMany;
//import javax.persistence.ManyToOne;
//import javax.persistence.OneToOne;
//import javax.persistence.Table;
//
//import org.hibernate.annotations.GenericGenerator;

import com.bridgelabz.todo.user.models.User;

//@Entity
//@Table
public class NoteExtras {

//	@Id
//	@GeneratedValue(generator = "noteextrasidgenerator", strategy = GenerationType.AUTO)
//	@GenericGenerator(name = "noteextrasidgenerator", strategy = "native")
	private long id;

	private boolean isPinned;

	private boolean isArchived;

	private boolean isTrashed;

	private String color;

	private Date reminder;

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "noteId")
	private Note note;

//	@OneToOne(fetch = FetchType.LAZY)
	private User owner;

//	@ManyToMany
//	@JoinTable(name = "NoteLabel", joinColumns = { @JoinColumn(name = "NEId") }, inverseJoinColumns = {
//			@JoinColumn(name = "labelId") })
	private Set<Label> labels = new HashSet<>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public Note getNote() {
		return note;
	}

	public void setNote(Note note) {
		this.note = note;
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

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Set<Label> getLabels() {
		return labels;
	}

	public void setLabels(Set<Label> labels) {
		this.labels = labels;
	}

}

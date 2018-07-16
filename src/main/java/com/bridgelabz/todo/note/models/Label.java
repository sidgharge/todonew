package com.bridgelabz.todo.note.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.bridgelabz.todo.user.models.User;

@Entity
@Table
public class Label {

	@Id
	@GenericGenerator(name = "labelIdGenerator", strategy = "native")
	@GeneratedValue(generator = "labelIdGenerator", strategy = GenerationType.AUTO)
	private long id;

	private String name;

	@ManyToMany(mappedBy = "labels")
	private List<NoteExtras> noteExtras;

	@ManyToOne
	private User owner;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<NoteExtras> getNoteExtras() {
		return noteExtras;
	}

	public void setNoteExtras(List<NoteExtras> noteExtras) {
		this.noteExtras = noteExtras;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

}

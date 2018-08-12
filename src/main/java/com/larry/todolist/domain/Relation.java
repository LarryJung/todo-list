package com.larry.todolist.domain;


import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Relation extends AbstractEntity{

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_relation_owner"))
	private User owner;

	@ManyToOne
	private User friend;

	public Relation() {
	}

	public Relation(User owner, User sender) {
		super(0L);
		this.owner = owner;
		this.friend = sender;
	}

	public User getOwner() {
		return owner;
	}

	public User getFriend() {
		return friend;
	}


}

package com.larry.todolist.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@NoArgsConstructor
@Getter
@Entity
public class Relation extends AbstractEntity{

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_relation_master"))
	private Task master;

	@ManyToOne
	private Task sub;

	private Relation(Task master, Task sub) {
		this.master = master;
		this.sub = sub;
	}

	public static Relation masterAndSub(Task master, Task sub) {
		return new Relation(master, sub);
	}

	public boolean isSubTaskCompleted(Task questioner) {
		if (sub.equals(questioner)) {
			return true;
		}
		return sub.wasCompleted();
	}

	@Override
	public String toString() {
		return "Relation{" +
				"master=" + master.getTodo() +
				", sub=" + sub.getTodo() +
				'}';
	}
}

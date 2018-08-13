package com.larry.todolist.domain;

import com.larry.todolist.domain.support.AbstractEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Entity
public class Relation extends AbstractEntity {

	@ManyToOne
	@JoinColumn(name = "master_id")
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

	public boolean isSubTaskCompleted() {
		return sub.wasCompleted();
	}

	@Override
	public String toString() {
		return "Relation{" +
				"master=" + master.getTodo() +
				", sub=" + sub.getTodo() +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		Relation relation = (Relation) o;
		return Objects.equals(master, relation.master) &&
				Objects.equals(sub, relation.sub);
	}

	@Override
	public int hashCode() {

		return Objects.hash(super.hashCode(), master, sub);
	}
}

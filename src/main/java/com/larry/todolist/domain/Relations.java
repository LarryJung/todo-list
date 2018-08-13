package com.larry.todolist.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Data
@Embeddable
@JsonIgnoreProperties({"createdDate", "modifiedDate", "pk"})
public class Relations {

	@JsonUnwrapped
	@OneToMany(mappedBy = "master")
	private List<Relation> relations = new ArrayList<>();

	public Relations() {

	}

	public Relations(List<Relation> relations) {
		this.relations = relations;
	}

	public List<Relation> getRelations() {
		return relations;
	}

	public boolean isSubTaskAllCompleted(Task questioner) {
		return relations.stream().filter(r -> r.getMaster().equals(questioner)).allMatch(r -> r.isSubTaskCompleted());
	}

	public Object getNotCompletedSubTaskList() {
		return null;
	}

	public Relations addRelation(Relation relation) {
		this.relations.add(relation);
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		Relations relations1 = (Relations) o;
		return Objects.equals(relations, relations1.relations);
	}

	@Override
	public int hashCode() {

		return Objects.hash(super.hashCode(), relations);
	}
}

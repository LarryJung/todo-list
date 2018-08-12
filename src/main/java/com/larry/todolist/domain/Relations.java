package com.larry.todolist.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Embeddable
@JsonIgnoreProperties({"createdDate", "modifiedDate", "id"})
public class Relations {

	@JsonUnwrapped
	@OneToMany(mappedBy = "master")
	private Set<Relation> relations = new HashSet<>();

	public Relations() {

	}

	public Relations(Set<Relation> relations) {
		this.relations = relations;
	}

	public Set<Relation> getRelations() {
		return relations;
	}

	public boolean isSubTaskAllCompleted(Task questioner) {
		return relations.stream().allMatch(r -> r.isSubTaskCompleted(questioner));
	}

	public Object getNotCompletedSubTaskList() {
		return null;
	}

	public Relations addRelation(Relation relation) {
		this.relations.add(relation);
		return this;
	}

}

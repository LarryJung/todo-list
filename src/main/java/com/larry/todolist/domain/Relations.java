package com.larry.todolist.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Embeddable
public class Relations {

	@OneToMany(mappedBy = "master")
	private List<Relation> relations = new ArrayList<>();

	public Relations(List<Relation> relations) {
		this.relations =relations;
	}

	public List<Relation> getRelations() {
		return relations;
	}

}

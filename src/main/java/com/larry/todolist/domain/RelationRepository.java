package com.larry.todolist.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RelationRepository extends JpaRepository<Relation, Long> {

	Optional<Relation> findByOwnerAndFriend(User owner, User friend);

	List<Relation> findByOwner(User loginUser);


}

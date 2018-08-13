package com.larry.todolist;

import com.larry.todolist.domain.Relation;
import com.larry.todolist.domain.Relations;
import com.larry.todolist.domain.Task;
import com.larry.todolist.domain.repository.RelationRepository;
import com.larry.todolist.domain.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Arrays;

@EnableJpaAuditing
@SpringBootApplication
public class TodolistApplication {

	private final Logger log = LoggerFactory.getLogger(TodolistApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(TodolistApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(TaskRepository taskRepository, RelationRepository relationRepository) {
		return (args) -> {
			Task chores = Task.of("집안일");
			Task laundry = Task.of("빨래");
			Task cleaning = Task.of("청소");
			Task cleaningRoom = Task.of( "방청소");

			Relation relation1 = Relation.masterAndSub(chores, laundry);
			Relation relation2 = Relation.masterAndSub(chores, cleaning);
			Relation relation3 = Relation.masterAndSub(chores, cleaningRoom);
			Relation relation4 = Relation.masterAndSub(cleaning, cleaningRoom);

			chores.registerRelations(new Relations(Arrays.asList(relation1, relation2, relation3)));
			laundry.registerRelations(new Relations(Arrays.asList(relation1)));
			cleaning.registerRelations(new Relations(Arrays.asList(relation2, relation4)));
			cleaningRoom.registerRelations(new Relations(Arrays.asList(relation3, relation4)));

			taskRepository.save(chores);
			taskRepository.save(laundry);
			taskRepository.save(cleaning);
			taskRepository.save(cleaningRoom);

			relationRepository.save(relation1);
			relationRepository.save(relation2);
			relationRepository.save(relation3);
			relationRepository.save(relation4);

			for (int i = 0; i < 30; i++) {
				taskRepository.save(Task.of(String.format("할일% d", i)));
			}

		};
	}
}

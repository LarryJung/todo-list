package com.larry.todolist;

import com.larry.todolist.domain.Task;
import com.larry.todolist.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TodolistApplication {

	private final Logger log = LoggerFactory.getLogger(TodolistApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(TodolistApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner demo(TaskRepository repository) {
//		return (args) -> {
//			// registerTask a couple of customers
//			Task chores = Task.of("집안일");
//			Task laundry = Task.of("빨래");
//			Task cleaning = Task.of("청소");
//			Task cleaningRoom = Task.of("방청소");
//
////
////			chores.addSubTask(laundry);
////			chores.addSubTask(cleaning);
////			chores.addSubTask(cleaningRoom);
////			cleaning.addSubTask(cleaningRoom);
//
//			repository.save(chores);
//			repository.save(laundry);
//			repository.save(cleaning);
//			repository.save(cleaningRoom);
//		};
//	}
}

package com.larry.todolist;

import com.larry.todolist.domain.Task;
import com.larry.todolist.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TodolistApplication {

	private final Logger log = LoggerFactory.getLogger(TodolistApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(TodolistApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(TaskService taskService) {
		return (args) -> {
			// save a couple of customers
			Task chores = Task.of("집안일");
			Task laundry = Task.of("빨래");
			Task cleaning = Task.of("청소");
			Task cleaningRoom = Task.of("방청소");

			chores.addSubTask(laundry);
			chores.addSubTask(cleaning);
			chores.addSubTask(cleaningRoom);
			cleaning.addSubTask(cleaningRoom);

			taskService.save(chores);
			taskService.save(laundry);
			taskService.save(cleaning);
			taskService.save(cleaningRoom);
		};
	}
}

package com.larry.todolist;

import com.larry.todolist.domain.Task;
import com.larry.todolist.domain.repository.TaskRepository;
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
//			for (int i = 0; i < 34; i++) {
//				repository.save(Task.of(String.format("%d%d%d%d", i,i,i,i)));
//			}
//		};
//	}
}

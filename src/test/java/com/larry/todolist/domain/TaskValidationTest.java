package com.larry.todolist.domain;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TaskValidationTest {

    private final Logger log = LoggerFactory.getLogger(TaskValidationTest.class);

    private static Validator validator;

    @BeforeClass
    public static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void minTodoTest() throws Exception {
        Task task = Task.of("a");
        Set<ConstraintViolation<Task>> constraintViolcations = validator.validate(task);
        assertThat(constraintViolcations.size(), is(1));

        for (ConstraintViolation<Task> constraintViolation : constraintViolcations) {
            log.debug("violation error message : {}", constraintViolation.getMessage());
        }
    }

    @Test
    public void maxTodoTest() throws Exception {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 21; i++) {
            sb.append("a");
        }
        Task task = Task.of(sb.toString());
        Set<ConstraintViolation<Task>> constraintViolcations = validator.validate(task);
        assertThat(constraintViolcations.size(), is(1));

        for (ConstraintViolation<Task> constraintViolation : constraintViolcations) {
            log.debug("violation error message : {}", constraintViolation.getMessage());
        }
    }
}

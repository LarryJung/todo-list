package com.larry.todolist.domain;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ReferencesTest {

    @Test
    public void allCompletedTest_pass() {
        Task task1 = Task.of("할일1").completeTask();
        Task task2 = Task.of("할일2").completeTask();
        References references = new References(Arrays.asList(task1, task2));
        assertTrue(references.isAllCompleted());
    }

    @Test
    public void allCompletedTest_fail() {
        Task task1 = Task.of("할일1").completeTask();
        Task task2 = Task.of("할일2");
        References references = new References(Arrays.asList(task1, task2));
        assertFalse(references.isAllCompleted());
    }
}

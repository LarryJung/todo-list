//package com.larry.todolist.domain;
//
//import org.junit.Test;
//
//import java.util.Arrays;
//
//import static org.hamcrest.CoreMatchers.is;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertThat;
//import static org.junit.Assert.assertTrue;
//
//public class ReferencesTest {
//
//    @Test
//    public void allCompletedTest_pass() {
//        Task task1 = Task.of("할일1").completeTask();
//        Task task2 = Task.of("할일2").completeTask();
//        References references = new References(Arrays.asList(task1, task2));
//        assertTrue(references.isAllCompleted());
//    }
//
//    @Test
//    public void allCompletedTest_fail() {
//        Task task1 = Task.of("할일1").completeTask();
//        Task task2 = Task.of("할일2");
//        References references = new References(Arrays.asList(task1, task2));
//        assertFalse(references.isAllCompleted());
//    }
//
//    @Test
//    public void getNotCompletedList() {
//        Task task1 = Task.of(1L, "할일1").completeTask();
//        Task task2 = Task.of(2L, "할일2");
//        Task task3 = Task.of(3L, "할일3");
//        Task task4 = Task.of(4L, "할일4");
//        References references = new References(Arrays.asList(task1, task2, task3, task4));
//        assertThat(references.getNotCompletedList(), is(Arrays.asList(2L, 3L, 4L)));
//    }
//}

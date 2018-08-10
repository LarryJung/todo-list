package com.larry.todolist.domain;

import com.larry.todolist.exceptionHandle.CannotCompleteException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TaskTest {

    private Task chores = Task.of(1L, "집안일");
    private Task laundry = Task.of(2L, "빨래");
    private Task cleaning = Task.of(3L, "청소");
    private Task cleaningRoom = Task.of(4L, "방청소");

    @Before
    public void setup() {
        chores.addSubTask(laundry);
        chores.addSubTask(cleaning);
        chores.addSubTask(cleaningRoom);
        cleaning.addSubTask(cleaningRoom);
    }

    @Test
    public void addReferenceTest() {
        assertThat(chores.getSubTasks(), is(new References(Arrays.asList(laundry, cleaning, cleaningRoom))));
        assertThat(laundry.getSubTasks(), is(new References()));
        assertThat(cleaning.getSubTasks(), is(new References(Arrays.asList(cleaningRoom))));
        assertThat(cleaningRoom.getSubTasks(), is(new References()));
        assertThat(cleaningRoom.getMasterTasks(), is(new References(Arrays.asList(chores, cleaning))));
    }

    @Test(expected = CannotCompleteException.class)
    public void completeTest_not_completed_at_subTask() {
        cleaning.completeTask();
    }

    @Test
    public void completeTest_completed_at_subTask() {
        cleaningRoom.completeTask();
        cleaning.completeTask();
        assertTrue(cleaning.wasCompleted());
    }

    @Test(expected = CannotCompleteException.class)
    public void completeTest_completed_fail_at_one_subTask() {
        cleaningRoom.completeTask();
        cleaning.completeTask();
        // except for laundry
        chores.completeTask();
    }

    @Test
    public void completeTest_all_pass() {
        cleaningRoom.completeTask();
        cleaning.completeTask();
        laundry.completeTask();
        chores.completeTask();
        assertTrue(chores.wasCompleted());
    }
}

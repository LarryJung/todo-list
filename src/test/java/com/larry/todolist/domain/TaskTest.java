package com.larry.todolist.domain;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TaskTest {

    private Task chores = Task.of("집안일");
    private Task laundry = Task.of("빨래");
    private Task cleaning = Task.of("청소");
    private Task cleaningRoom = Task.of("방청소");

    @Before
    public void setup() {
        chores.addSubTask(laundry);
        chores.addSubTask(cleaning);
        chores.addSubTask(cleaningRoom);
        cleaning.addSubTask(cleaningRoom);
    }

    @Test
    public void addReferenceTest() {
        assertNull(chores.getMasterTasks());
        assertThat(chores.getSubTasks().toString(), is("빨래,청소,방청소"));

        assertThat(laundry.getMasterTasks().toString(), is("집안일"));
        assertNull(laundry.getSubTasks());

        assertThat(cleaning.getMasterTasks().toString(), is("집안일"));
        assertThat(cleaning.getSubTasks().toString(), is("방청소"));

        assertThat(cleaningRoom.getMasterTasks().toString(), is("집안일,청소"));
        assertNull(cleaningRoom.getSubTasks());
    }

    @Test(expected = RuntimeException.class)
    public void completeTest_not_completed_at_subTask() {
        cleaning.completeTask();
    }

    @Test
    public void completeTest_completed_at_subTask() {
        cleaningRoom.completeTask();
        cleaning.completeTask();
        assertTrue(cleaning.wasCompleted());
    }

    @Test(expected = RuntimeException.class)
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

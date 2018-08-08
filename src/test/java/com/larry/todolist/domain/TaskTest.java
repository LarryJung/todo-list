package com.larry.todolist.domain;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TaskTest {

    @Test
    public void addReferenceTest() {
        Task chores = Task.of("집안일");
        Task laundry = Task.of("빨래");
        Task cleaning = Task.of("청소");
        Task cleaningRoom = Task.of("방청소");

        chores.addReferenceTask(laundry, cleaning, cleaningRoom);
        cleaning.addReferenceTask(cleaningRoom);

        assertThat(chores.getReferences().toString(), is("빨래,청소,방청소"));
        assertThat(laundry.getReferences().toString(), is("집안일"));
        assertThat(cleaning.getReferences().toString(), is("집안일,방청소"));
        assertThat(cleaningRoom.getReferences().toString(), is("집안일,청소"));
    }


}

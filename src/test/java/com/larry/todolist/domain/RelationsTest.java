package com.larry.todolist.domain;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RelationsTest {

    private Task chores = Task.of(1L, "집안일");
    private Task laundry = Task.of(2L, "빨래");

    @Test
    public void allCompleteTest() {
        Relation relation1 = Relation.masterAndSub(chores, laundry);
        Relations relations1= new Relations(Arrays.asList(relation1));
        assertFalse(relations1.isSubTaskAllCompleted(chores));
        laundry.completeTask();
        assertTrue(relation1.isSubTaskCompleted());
    }

}

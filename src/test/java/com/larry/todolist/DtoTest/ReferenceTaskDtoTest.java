package com.larry.todolist.DtoTest;

import com.larry.todolist.domain.support.TaskType;
import com.larry.todolist.domain.dto.requestDto.ReferenceTaskDto;
import org.junit.Test;

public class ReferenceTaskDtoTest {

    @Test
    public void checkDuplicatesTest() {
        ReferenceTaskDto taskDto1 = new ReferenceTaskDto(TaskType.MASTER, new Long[] {1L, 2L, 3L});
        ReferenceTaskDto taskDto2 = new ReferenceTaskDto(TaskType.MASTER, new Long[] {4L, 5L, 6L});
        taskDto1.checkDuplicates(taskDto2);
    }

    @Test(expected = RuntimeException.class)
    public void checkDuplicatesTest_fail() {
        ReferenceTaskDto taskDto1 = new ReferenceTaskDto(TaskType.MASTER, new Long[] {1L, 2L, 3L});
        ReferenceTaskDto taskDto2 = new ReferenceTaskDto(TaskType.MASTER, new Long[] {1L, 5L, 6L});
        taskDto1.checkDuplicates(taskDto2);
    }
}

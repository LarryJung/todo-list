package com.larry.todolist.domain.support;

import com.larry.todolist.domain.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public enum TaskType {

    MASTER, SUB;

    private final Logger log = LoggerFactory.getLogger(TaskType.class);

    // 스트레티지 패턴을 사용해서 다르게 해보자.
    public Method retrieveMethod(Task target) {
        log.info("this enum is : {}", this);
        String methodName = String.format("add%sTask", makeFirstUpperCase(this.name()));
        try {
            return target.getClass().getDeclaredMethod(methodName, Task.class);
        } catch (NoSuchMethodException e) {
            e.getStackTrace();
        }
        return null;
    }

    private String makeFirstUpperCase(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}

package com.larry.todolist.domain.support;

import com.larry.todolist.domain.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public enum TaskType {

    MASTER, SUB;

    private final Logger log = LoggerFactory.getLogger(TaskType.class);

    public boolean isMaster() {
        return this.equals(MASTER);
    }

    public boolean isSub() {
        return this.equals(SUB);
    }

    public Method retrieveMethod(Task target) {
        log.info("this enum is : {}", this);
        if (isMaster()) {
            try {
                return target.getClass().getDeclaredMethod("addSubTask", Task.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        if (isSub()) {
            try {
                return target.getClass().getDeclaredMethod("addMasterTask", Task.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

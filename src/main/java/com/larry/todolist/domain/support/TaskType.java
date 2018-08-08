package com.larry.todolist.domain.support;

import com.larry.todolist.domain.Task;

import java.lang.reflect.Method;

public enum TaskType {
    MASTER, SUB;

    public boolean isMaster() {
        return this.equals(MASTER);
    }

    public boolean isSub() {
        return this.equals(SUB);
    }

    public Method retrieveMethod(Task target) {
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

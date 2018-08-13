package com.larry.todolist.domain.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.larry.todolist.domain.Task;

import java.util.ArrayList;
import java.util.List;

public class ReferenceShowDto {

    @JsonProperty("masterTasks")
    private List<LightTask> master = new ArrayList<>();

    @JsonProperty("subTasks")
    private List<LightTask> sub = new ArrayList<>();

    public void addMaster(Task master) {
        this.master.add(new LightTask(master.getId(), master.getTodo()));
    }

    public void addSub(Task sub) {
        this.sub.add(new LightTask(sub.getId(), sub.getTodo()));
    }

    private static class LightTask {
        @JsonProperty("id")
        private Long id;
        @JsonProperty("todo")
        private String todo;

        public LightTask(Long id, String todo) {
            this.id = id;
            this.todo = todo;
        }
    }

}

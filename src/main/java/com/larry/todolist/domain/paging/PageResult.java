package com.larry.todolist.domain.paging;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.larry.todolist.domain.Task;

import java.util.List;

public class PageResult {

    @JsonProperty("paging")
    private PagingDto paging;

    @JsonProperty("list")
    private List<Task> taskList;

    public PageResult(PagingDto pagingDto, List<Task> taskList) {
        this.paging = pagingDto;
        this.taskList = taskList;
    }
}

package com.larry.todolist.domain.paging;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.larry.todolist.domain.Task;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class PageResult {

    @JsonUnwrapped
    private PagingDto pagingDto;

    @JsonProperty("list")
    private List<Task> taskList;

    public PageResult(PagingDto pagingDto, List<Task> taskList) {
        this.pagingDto = pagingDto;
        this.taskList = taskList;
    }
}

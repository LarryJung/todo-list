package com.larry.todolist.domain.paging;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PagingDto {

    @JsonProperty("startPage")
    private int startPage;
    @JsonProperty("endPage")
    private int endPage;
    @JsonProperty("totalBlock")
    private int totalBlock;
    @JsonProperty("totalPage")
    private int totalPage;
    @JsonProperty("blockPageNum")
    private int blockPageNum;
    @JsonProperty("totalCount")
    private int totalCount;
    @JsonProperty("block")
    private int block;
    @JsonProperty("page")
    private int page;

    @Builder
    public PagingDto(int startPage, int endPage, int totalBlock, int totalPage, int blockPageNum, int totalCount, int block, int page) {
        this.startPage = startPage;
        this.endPage = endPage;
        this.totalBlock = totalBlock;
        this.totalPage = totalPage;
        this.blockPageNum = blockPageNum;
        this.totalCount = totalCount;
        this.block = block;
        this.page = page;
    }
}

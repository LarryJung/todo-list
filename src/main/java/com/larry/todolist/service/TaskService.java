package com.larry.todolist.service;

import com.larry.todolist.domain.*;
import com.larry.todolist.domain.paging.PageResult;
import com.larry.todolist.domain.paging.PagingDto;
import com.larry.todolist.domain.dto.requestDto.TaskRequestDto;
import com.larry.todolist.domain.dto.requestDto.UpdateDto;
import com.larry.todolist.domain.dto.responseDto.ReferenceShowDto;
import com.larry.todolist.domain.repository.RelationRepository;
import com.larry.todolist.domain.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class TaskService {

    private final Logger log = LoggerFactory.getLogger(TaskService.class);

    public static final int PAGES_PER_BLOCK = 3;


    @Resource(name = "taskRepository")
    private TaskRepository taskRepository;

    @Resource(name = "relationRepository")
    private RelationRepository relationRepository;

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findById(Long id) {
        return taskRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Task findByTodo(String todo) {
        return taskRepository.findByTodo(todo).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public Task registerTask(Task presentTask, TaskRequestDto dto) {
        log.info("input dto : {}", dto);
        List<IdRelation> newRelations = dto.makeIdRelations(presentTask.getId());
        return registerReferences(presentTask, newRelations);
    }

    @Transactional
    public Task registerTask(TaskRequestDto dto) {
        log.info("input dto : {}", dto);
        Task newTask = taskRepository.save(dto.toEntity());
        List<IdRelation> newRelations = dto.makeIdRelations(newTask.getId());
        return registerReferences(newTask, newRelations);
    }

    public Task registerReferences(Task presentTask, List<IdRelation> idRelations) {
        idRelations.forEach(this::checkRelation);
        idRelations.forEach(r -> r.registerRelation(taskRepository, relationRepository));
        return presentTask;
    }

    private void checkRelation(IdRelation r) {
        relationRepository.findAllBySubId(r.getMasterId())
                .forEach(ref -> relationRepository.findAllBySubId(ref.getMaster().getId())
                        .forEach(rr -> {
                            if (rr.getMaster().equals(findById(r.getSubId()))) {
                                throw new RuntimeException("참조관계가 이상합니다. - 순환");
                            }
                        }));
    }

    @Transactional
    public Task complete(Long presentTaskId) {
        return findById(presentTaskId).completeTask();
    }

    @Transactional
    public Task update(UpdateDto updateDto) {
        return findById(updateDto.getPk()).updateTodo(updateDto.getValue());
    }


    public ReferenceShowDto findRelations(Long presentTaskId) {
        ReferenceShowDto referenceShowDto = new ReferenceShowDto();
        relationRepository.findAllBySubId(presentTaskId).forEach(t -> referenceShowDto.addMaster(t.getMaster()));
        relationRepository.findAllByMasterId(presentTaskId).forEach(t -> referenceShowDto.addSub(t.getSub()));
        return referenceShowDto;
    }

    public PageResult findPageByComplete(Pageable pageable, boolean complete) {
        long totalCount = 0;
        int pages = 0;
        int pNo = pageable.getPageNumber();
        Page<Task> list;
        if (complete) {
            totalCount = taskRepository.countByCompletedDateIsNotNull();
            pages = (int)totalCount / pageable.getPageSize();
            list = taskRepository.findAllByCompletedDateIsNotNull(pageable);
        } else {
            totalCount = taskRepository.countByCompletedDateIsNull();
            pages = (int)totalCount / pageable.getPageSize();
            list = taskRepository.findAllByCompletedDateIsNull(pageable);
        }
        log.info("page list? {}", list);
        PagingDto pagingDto = PagingDto.builder()
                .startPage(((pNo/ PAGES_PER_BLOCK)*PAGES_PER_BLOCK))
                .endPage(((pNo/PAGES_PER_BLOCK)*PAGES_PER_BLOCK) + (PAGES_PER_BLOCK-1))
                .totalBlock(((((int)totalCount)%(PAGES_PER_BLOCK*pageable.getPageSize())) == 0 ? (((int)totalCount)/(PAGES_PER_BLOCK*pageable.getPageSize())) : (((int)totalCount)/(PAGES_PER_BLOCK*pageable.getPageSize())) + 1))
                .totalPage(pages)
                .blockPageNum(PAGES_PER_BLOCK)
                .totalCount((int) totalCount)
                .block(pNo / PAGES_PER_BLOCK)
                .page(pNo).build();
        List<Task> tasks = list.getContent();
        return new PageResult(pagingDto, tasks);
    }

    @Transactional
    public void delete(Long presentTaskId) {
        taskRepository.deleteById(presentTaskId);
    }
}

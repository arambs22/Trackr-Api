package com.aram.taskflow.controller;

import com.aram.taskflow.dto.*;
import com.aram.taskflow.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/boards/{boardId}/lists/{listId}/tasks")
    public TaskResponse create(
            Authentication auth,
            @PathVariable Long boardId,
            @PathVariable Long listId,
            @Valid @RequestBody CreateTaskRequest req
    ) {
        return taskService.create((String) auth.getPrincipal(), boardId, listId, req);
    }

    @GetMapping("/boards/{boardId}/lists/{listId}/tasks")
    public List<TaskResponse> getAll(
            Authentication auth,
            @PathVariable Long boardId,
            @PathVariable Long listId
    ) {
        return taskService.getAll((String) auth.getPrincipal(), boardId, listId);
    }

    @PutMapping("/boards/{boardId}/lists/{listId}/tasks/{taskId}")
    public TaskResponse update(
            Authentication auth,
            @PathVariable Long boardId,
            @PathVariable Long listId,
            @PathVariable Long taskId,
            @Valid @RequestBody UpdateTaskRequest req
    ) {
        return taskService.update((String) auth.getPrincipal(), boardId, listId, taskId, req);
    }

    @DeleteMapping("/boards/{boardId}/lists/{listId}/tasks/{taskId}")
    public void delete(
            Authentication auth,
            @PathVariable Long boardId,
            @PathVariable Long listId,
            @PathVariable Long taskId
    ) {
        taskService.delete((String) auth.getPrincipal(), boardId, listId, taskId);
    }

    @PatchMapping("/boards/{boardId}/lists/{listId}/tasks/{taskId}/move")
    public TaskResponse move(
            Authentication auth,
            @PathVariable Long boardId,
            @PathVariable Long listId,
            @PathVariable Long taskId,
            @RequestParam Long newListId
    ) {
        return taskService.move((String) auth.getPrincipal(), boardId, listId, taskId, newListId);
    }
}
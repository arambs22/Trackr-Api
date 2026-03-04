package com.aram.taskflow.controller;

import com.aram.taskflow.dto.CreateListRequest;
import com.aram.taskflow.dto.ListResponse;
import com.aram.taskflow.dto.UpdateListRequest;
import com.aram.taskflow.service.ListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ListController {

    private final ListService listService;

    @PostMapping("/boards/{boardId}/lists")
    public ListResponse create(
            Authentication auth,
            @PathVariable Long boardId,
            @Valid @RequestBody CreateListRequest req
    ) {
        return listService.create((String) auth.getPrincipal(), boardId, req);
    }

    @GetMapping("/boards/{boardId}/lists")
    public List<ListResponse> getAll(Authentication auth, @PathVariable Long boardId) {
        return listService.getAll((String) auth.getPrincipal(), boardId);
    }

    @PutMapping("/boards/{boardId}/lists/{listId}")
    public ListResponse update(
            Authentication auth,
            @PathVariable Long boardId,
            @PathVariable Long listId,
            @Valid @RequestBody UpdateListRequest req
    ) {
        return listService.update((String) auth.getPrincipal(), boardId, listId, req);
    }

    @DeleteMapping("/boards/{boardId}/lists/{listId}")
    public void delete(
            Authentication auth,
            @PathVariable Long boardId,
            @PathVariable Long listId
    ) {
        listService.delete((String) auth.getPrincipal(), boardId, listId);
    }
}
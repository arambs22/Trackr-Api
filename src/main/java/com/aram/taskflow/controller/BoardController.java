package com.aram.taskflow.controller;

import com.aram.taskflow.dto.BoardResponse;
import com.aram.taskflow.dto.CreateBoardRequest;
import com.aram.taskflow.dto.UpdateBoardRequest;
import com.aram.taskflow.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public BoardResponse create(
            Authentication auth,
            @Valid @RequestBody CreateBoardRequest req
    ) {
        return boardService.create((String) auth.getPrincipal(), req);
    }

    @GetMapping
    public List<BoardResponse> getAll(Authentication auth) {
        return boardService.getAll((String) auth.getPrincipal());
    }

    @GetMapping("/{id}")
    public BoardResponse getById(
            Authentication auth,
            @PathVariable Long id
    ) {
        return boardService.getById((String) auth.getPrincipal(), id);
    }

    @PutMapping("/{id}")
    public BoardResponse update(
            Authentication auth,
            @PathVariable Long id,
            @Valid @RequestBody UpdateBoardRequest req
    ) {
        return boardService.update((String) auth.getPrincipal(), id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(Authentication auth, @PathVariable Long id) {
        boardService.delete((String) auth.getPrincipal(), id);
    }
}
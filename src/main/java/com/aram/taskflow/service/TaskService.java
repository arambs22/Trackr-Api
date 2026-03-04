package com.aram.taskflow.service;

import com.aram.taskflow.domain.*;
import com.aram.taskflow.dto.*;
import com.aram.taskflow.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final BoardRepository boardRepository;
    private final BoardListRepository listRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private Board getOwnedBoard(String email, Long boardId) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return boardRepository.findByIdAndOwner(boardId, user).orElseThrow();
    }

    private BoardList getOwnedList(String email, Long boardId, Long listId) {
        Board board = getOwnedBoard(email, boardId);
        return listRepository.findByIdAndBoard(listId, board).orElseThrow();
    }

    public TaskResponse create(String email, Long boardId, Long listId, CreateTaskRequest req) {
        BoardList list = getOwnedList(email, boardId, listId);

        Task task = Task.builder()
                .title(req.title())
                .description(req.description())
                .position(req.position())
                .list(list)
                .build();

        taskRepository.save(task);

        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getPosition());
    }

    public List<TaskResponse> getAll(String email, Long boardId, Long listId) {
        BoardList list = getOwnedList(email, boardId, listId);

        return taskRepository.findByListOrderByPosition(list)
                .stream()
                .map(t -> new TaskResponse(t.getId(), t.getTitle(), t.getDescription(), t.getPosition()))
                .toList();
    }

    public TaskResponse update(String email, Long boardId, Long listId, Long taskId, UpdateTaskRequest req) {
        BoardList list = getOwnedList(email, boardId, listId);

        Task task = taskRepository.findByIdAndList(taskId, list).orElseThrow();
        task.setTitle(req.title());
        task.setDescription(req.description());
        task.setPosition(req.position());
        taskRepository.save(task);

        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getPosition());
    }

    public void delete(String email, Long boardId, Long listId, Long taskId) {
        BoardList list = getOwnedList(email, boardId, listId);

        Task task = taskRepository.findByIdAndList(taskId, list).orElseThrow();
        taskRepository.delete(task);
    }

    public TaskResponse move(String email, Long boardId, Long listId, Long taskId, Long newListId) {
        BoardList list = getOwnedList(email, boardId, listId);
        BoardList newList = getOwnedList(email, boardId, newListId);

        Task task = taskRepository.findByIdAndList(taskId, list).orElseThrow();
        task.setList(newList);
        taskRepository.save(task);

        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getPosition());
    }
}
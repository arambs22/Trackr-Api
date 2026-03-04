package com.aram.taskflow.repository;

import com.aram.taskflow.domain.BoardList;
import com.aram.taskflow.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByListOrderByPosition(BoardList list);
    Optional<Task> findByIdAndList(Long id, BoardList list);
}
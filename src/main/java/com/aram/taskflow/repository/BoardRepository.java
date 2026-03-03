package com.aram.taskflow.repository;

import com.aram.taskflow.domain.Board;
import com.aram.taskflow.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByOwner(User owner);

    Optional<Board> findByIdAndOwner(Long id, User owner);
}
package com.aram.taskflow.repository;

import com.aram.taskflow.domain.Board;
import com.aram.taskflow.domain.BoardList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardListRepository extends JpaRepository<BoardList, Long> {

    List<BoardList> findByBoardOrderByPositionAsc(Board board);

    Optional<BoardList> findByIdAndBoard(Long id, Board board);
}
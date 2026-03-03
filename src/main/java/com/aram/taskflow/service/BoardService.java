package com.aram.taskflow.service;

import com.aram.taskflow.domain.Board;
import com.aram.taskflow.domain.User;
import com.aram.taskflow.dto.BoardResponse;
import com.aram.taskflow.dto.CreateBoardRequest;
import com.aram.taskflow.repository.BoardRepository;
import com.aram.taskflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public BoardResponse create(String email, CreateBoardRequest req) {

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        Board board = Board.builder()
                .name(req.name())
                .owner(user)
                .build();

        boardRepository.save(board);

        return new BoardResponse(
                board.getId(),
                board.getName(),
                board.getCreatedAt()
        );
    }

    public List<BoardResponse> getAll(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        return boardRepository.findByOwner(user)
                .stream()
                .map(b -> new BoardResponse(
                        b.getId(),
                        b.getName(),
                        b.getCreatedAt()
                ))
                .toList();
    }

    public BoardResponse getById(String email, Long id) {

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        Board board = boardRepository.findByIdAndOwner(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Board no encontrada"));

        return new BoardResponse(
                board.getId(),
                board.getName(),
                board.getCreatedAt()
        );
    }
}
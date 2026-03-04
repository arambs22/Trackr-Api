package com.aram.taskflow.service;

import com.aram.taskflow.domain.Board;
import com.aram.taskflow.domain.BoardList;
import com.aram.taskflow.domain.User;
import com.aram.taskflow.dto.CreateListRequest;
import com.aram.taskflow.dto.ListResponse;
import com.aram.taskflow.dto.UpdateListRequest;
import com.aram.taskflow.repository.BoardListRepository;
import com.aram.taskflow.repository.BoardRepository;
import com.aram.taskflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListService {

    private final BoardRepository boardRepository;
    private final BoardListRepository listRepository;
    private final UserRepository userRepository;

    private Board getOwnedBoard(String email, Long boardId) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return boardRepository.findByIdAndOwner(boardId, user)
                .orElseThrow(() -> new IllegalArgumentException("Board no encontrada"));
    }

    public ListResponse create(String email, Long boardId, CreateListRequest req) {
        Board board = getOwnedBoard(email, boardId);

        BoardList list = BoardList.builder()
                .name(req.name())
                .position(req.position())
                .board(board)
                .build();

        listRepository.save(list);

        return new ListResponse(list.getId(), list.getName(), list.getPosition());
    }

    public List<ListResponse> getAll(String email, Long boardId) {
        Board board = getOwnedBoard(email, boardId);

        return listRepository.findByBoardOrderByPositionAsc(board)
                .stream()
                .map(l -> new ListResponse(l.getId(), l.getName(), l.getPosition()))
                .toList();
    }

    public ListResponse update(String email, Long boardId, Long listId, UpdateListRequest req) {
        Board board = getOwnedBoard(email, boardId);

        BoardList list = listRepository.findByIdAndBoard(listId, board)
                .orElseThrow(() -> new IllegalArgumentException("List no encontrada"));

        list.setName(req.name());
        list.setPosition(req.position());
        listRepository.save(list);

        return new ListResponse(list.getId(), list.getName(), list.getPosition());
    }

    public void delete(String email, Long boardId, Long listId) {
        Board board = getOwnedBoard(email, boardId);

        BoardList list = listRepository.findByIdAndBoard(listId, board)
                .orElseThrow(() -> new IllegalArgumentException("List no encontrada"));

        listRepository.delete(list);
    }
}
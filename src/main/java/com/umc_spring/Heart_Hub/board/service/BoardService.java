package com.umc_spring.Heart_Hub.board.service;

import com.umc_spring.Heart_Hub.board.dto.BoardRequestDto;
import com.umc_spring.Heart_Hub.board.dto.BoardResponseDto;
import com.umc_spring.Heart_Hub.board.model.Board;
import com.umc_spring.Heart_Hub.board.repository.BoardRepository;
import com.umc_spring.Heart_Hub.constant.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    //등록
    @Transactional
    public Long boardRegister(final BoardRequestDto params){
        Board boardRegister = boardRepository.save(params.toEntity());
        Long id = boardRegister.getBoardId();
        return id;
    }

    /*
     게시글 리스트 조회
     */
    @Transactional
    public List<BoardResponseDto> findAll() {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        List<Board> list = boardRepository.findAll(sort);
        List<BoardResponseDto> responseList = list.stream().map(m -> new BoardResponseDto(m)).toList();
        return responseList;
    }
    @Transactional
    public BoardResponseDto findBoard(final Long id){
        Board findBoard = boardRepository.findById(id).get();
        BoardResponseDto boardResponseDto = new BoardResponseDto(findBoard);
        return boardResponseDto;
    }

    /*
     게시글 삭제
     */
    @Transactional
    public void delBoard(final Long id){
        if (boardRepository.findById(id).isPresent()){
            Board board = boardRepository.findById(id).get();
            boardRepository.delete(board);
        }
    }
}

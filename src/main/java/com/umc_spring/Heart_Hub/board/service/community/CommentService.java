package com.umc_spring.Heart_Hub.board.service.community;

import com.umc_spring.Heart_Hub.board.dto.community.BoardDto;
import com.umc_spring.Heart_Hub.board.dto.community.CommentDto;
import com.umc_spring.Heart_Hub.board.model.community.BlockedList;
import com.umc_spring.Heart_Hub.board.model.community.Board;
import com.umc_spring.Heart_Hub.board.model.community.Comment;
import com.umc_spring.Heart_Hub.board.repository.community.BlockedListRepository;
import com.umc_spring.Heart_Hub.board.repository.community.BoardRepository;
import com.umc_spring.Heart_Hub.board.repository.community.CommentGoodRepository;
import com.umc_spring.Heart_Hub.board.repository.community.CommentRepository;
import com.umc_spring.Heart_Hub.constant.enums.CustomResponseStatus;
import com.umc_spring.Heart_Hub.constant.exception.CustomException;
import com.umc_spring.Heart_Hub.user.model.User;
import com.umc_spring.Heart_Hub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CommentGoodRepository commentGoodRepository;
    private final BlockedListRepository blockedListRepository;

    /**
     * 해당 게시글 댓글 목록 조회
     */
    @Transactional
    public List<CommentDto.Response> findComments(BoardDto.BoardResponseDto boardResponse, String username) {
        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new CustomException(CustomResponseStatus.USER_NOT_FOUND);
        }

        List<User> blockedUsers = blockedListRepository.findAllByBlocker(user).stream()
                .map(BlockedList::getBlockedUser)
                .collect(Collectors.toList());

        Board board = boardRepository.findById(boardResponse.getBoardId()).orElseThrow(() -> {
            throw new CustomException(CustomResponseStatus.POST_NOT_FOUND);
        });

        List<Comment> comments = commentRepository.findAllByBoard(board);
        List<CommentDto.Response> commentResponse = new ArrayList<>();
        for (Comment c : comments) {
            if (!blockedUsers.contains(c.getUser())) {
                commentResponse.add(new CommentDto.Response(c));
            }
        }
        return commentResponse;
    }

    /**
     * 댓글 등록
     */
    @Transactional
    public Long createComment(CommentDto.Request replyRequest, String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new CustomException(CustomResponseStatus.USER_NOT_FOUND);
        }
        Board board = boardRepository.findById(replyRequest.getBoardId()).orElseThrow(() -> {
            throw new CustomException(CustomResponseStatus.POST_NOT_FOUND);
        });

        Comment replyComment = Comment.builder()
                .content(replyRequest.getContent())
                .user(user)
                .board(board)
                .build();

        Comment parent;
        if (replyRequest.getParentId() != null) {
            parent = commentRepository.findById(replyRequest.getParentId()).orElseThrow(() -> {
                throw new CustomException(CustomResponseStatus.COMMENT_PARENT_NOT_FOUND);
            });
            replyComment.updateParent(parent);
        }

        commentRepository.save(replyComment);
        return replyComment.getCommentId();
    }

    /**
     * 댓글 삭제
     */
    public CommentDto.DeleteResponse deleteComment(CommentDto.DeleteRequest deleteRequest, String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new CustomException(CustomResponseStatus.USER_NOT_FOUND);
        }

        Comment findComment = commentRepository.findById(deleteRequest.getCommentId()).orElseThrow(() -> {
            throw new CustomException(CustomResponseStatus.COMMENT_NOT_FOUND);
        });

        if(user.getUsername() != findComment.getUser().getUsername()) {
            throw new CustomException(CustomResponseStatus.USER_NOT_MATCH);
        }

        commentRepository.delete(findComment);
        return new CommentDto.DeleteResponse(deleteRequest.getCommentId());
    }

}

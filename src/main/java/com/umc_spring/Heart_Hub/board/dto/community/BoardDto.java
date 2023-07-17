package com.umc_spring.Heart_Hub.board.dto.community;

import com.umc_spring.Heart_Hub.board.model.community.Board;
import com.umc_spring.Heart_Hub.board.model.community.Comment;
import com.umc_spring.Heart_Hub.user.model.User;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


public class BoardDto {
    @Getter
    @Data
    @NoArgsConstructor
    public static class BoardRequestDto{
        private String content;
        private User user;
        private Board board;
        private List<Comment> commentList;

    }
    @Getter
    public static class BoardResponseDto {
        private final Long boardId;

        private String content;

        private String status;

        private User user;

        private List<CommentDto.Response> commentList;

        private LocalDateTime createdDate;
        @Builder
        public BoardResponseDto(Board board){
            this.boardId = board.getBoardId();
            this.content = board.getContent();
            this.status = board.getStatus();
            this.user = board.getUser();
            this.commentList = board.getComments().stream().map(CommentDto.Response::new).collect(Collectors.toList());;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class BlockUserReqDto {
        private String blockedUserName;
    }

    @Getter
    @NoArgsConstructor
    public static class BlockUserResDto {
        private String blockedUserName;

        @Builder
        public BlockUserResDto(User user) {
            this.blockedUserName = user.getUsername();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class UnblockUserReqDto {
        private String blockedUserName;
    }

    @Getter
    @NoArgsConstructor
    public static class UnblockUserResDto {
        private String blockedUserName;

        @Builder
        public UnblockUserResDto(User user) {
            this.blockedUserName = user.getUsername();
        }
    }

}

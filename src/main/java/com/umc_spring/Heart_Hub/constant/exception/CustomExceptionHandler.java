package com.umc_spring.Heart_Hub.constant.exception;

import com.umc_spring.Heart_Hub.constant.dto.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(BindingResult bindingResult) {
        log.info("Enter handleValidationException()");
        log.info("handlevalidateException실행");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.createFail(bindingResult));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<String>> handleCustomException(CustomException e) {
//        log.error(e.getErrorCode().getMessage());
        log.info("handleCustomException실행");
        log.info("e.getStatus : "+e.getResponseStatus().toString());
        log.info("e.getMessage : "+e.getResponseStatus().getMessage());
        return ResponseEntity.status(e.getResponseStatus().getCode())
                .body(ApiResponse.createError(e.getResponseStatus()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleException(CustomException e) {
        log.error(e.getMessage());
        log.info("handleException실행");
        return ResponseEntity.status(e.getResponseStatus().getCode())
                .body(ApiResponse.createError(e.getResponseStatus()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<String>> handleAccessDeniedException(CustomException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(e.getResponseStatus().getCode())
                .body(ApiResponse.createError(e.getResponseStatus()));
    }
}

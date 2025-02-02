package com.revup.global.exception;

import com.revup.error.AppException;
import com.revup.global.dto.ApiResponse;
import com.revup.global.util.ResponseUtil;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiResponse<Void>> handleException(final Exception e) {
        e.printStackTrace();
        return ResponseUtil.failure(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

    }

    /**
     * Application 내의 Exception 들 핸들링
     */

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Void>> handleAppException(AppException e) {
        return ResponseUtil.failure(e.getErrorCode().getHttpStatus(), e.getMessage());
    }

    /**
     * @Valid 검증 오류 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationExceptions(
            final MethodArgumentNotValidException e
    ) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("잘못된 요청입니다.");
        return ResponseUtil.failure(HttpStatus.BAD_REQUEST, errorMessage);
    }

}

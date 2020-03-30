package io.homo_efficio.ecotrip.global.error;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-03-30
 */
@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageResolver messageResolver;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorValue.INVALID_PARAMETER, messageResolver.getMessage(ErrorValue.INVALID_PARAMETER.getErrorCode()), e.getBindingResult());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}

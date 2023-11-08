package ru.practicum.shareit.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;

import javax.validation.ValidationException;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException exception) {
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(HttpStatus.NOT_FOUND.toString())
                //.error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .error(exception.getMessage())
                //.message(exception.getMessage())
                .build();
        log.error("{}", errorResponse.getError());
        return errorResponse;
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(ValidationException exception) {
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(HttpStatus.BAD_REQUEST.toString())
                .error(exception.getMessage())
                //.message(exception.getMessage())
                .build();
        log.error("{}", errorResponse.getError());
        return errorResponse;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(HttpStatus.BAD_REQUEST.toString())
                //.error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .error(exception.getMessage())
                //.message(exception.getMessage())
                .build();
        log.error("{}", errorResponse.getError());
        return errorResponse;
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(ConflictException exception) {
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(HttpStatus.CONFLICT.toString())
                //.error(HttpStatus.CONFLICT.getReasonPhrase())
                .error(exception.getMessage())
                //.message(exception.getMessage())
                .build();
        log.error("{}", errorResponse.getError());
        return errorResponse;
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbiddenException(ForbiddenException exception) {
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(HttpStatus.FORBIDDEN.toString())
                //.error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .error(exception.getMessage())
                //.message(exception.getMessage())
                .build();
        log.error("{}", errorResponse.getError());
        return errorResponse;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(Throwable exception) {
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                //.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .error(exception.getMessage())
                //.message(exception.getMessage())
                .build();
        log.error("{}", errorResponse.getError());
        return errorResponse;
    }
}
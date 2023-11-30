package ru.practicum.shareit.error;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;

import javax.validation.ValidationException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ErrorHandlerTest {
    private final ErrorHandler errorHandler;

    @Test
    void handleNotFoundException() {
        final NotFoundException exception = new NotFoundException("test");
        final ErrorResponse errorResponse = errorHandler.handleNotFoundException(exception);
        assertThat(errorResponse.getError()).isEqualTo(exception.getMessage());
    }

    @Test
    void handleValidationException() {
        final ValidationException exception = new ValidationException("test");
        final ErrorResponse errorResponse = errorHandler.handleValidationException(exception);
        assertThat(errorResponse.getError()).isEqualTo(exception.getMessage());
    }


    @Test
    void handleConflictException() {
        final ConflictException exception = new ConflictException("test");
        final ErrorResponse errorResponse = errorHandler.handleConflictException(exception);
        assertThat(errorResponse.getError()).isEqualTo(exception.getMessage());
    }

    @Test
    void handleForbiddenException() {
        final ForbiddenException exception = new ForbiddenException("test");
        final ErrorResponse errorResponse = errorHandler.handleForbiddenException(exception);
        assertThat(errorResponse.getError()).isEqualTo(exception.getMessage());
    }

    @Test
    void handleThrowable() {
        final Throwable exception = new Throwable("test");
        final ErrorResponse errorResponse = errorHandler.handleThrowable(exception);
        assertThat(errorResponse.getError()).isEqualTo(exception.getMessage());
    }
}
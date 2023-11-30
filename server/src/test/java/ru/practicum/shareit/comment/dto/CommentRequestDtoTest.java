package ru.practicum.shareit.comment.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.validation.Marker;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class CommentRequestDtoTest {
    private final JacksonTester<CommentRequestDto> jacksonTester;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private CommentRequestDto commentRequestDto;

    @BeforeEach
    void setUp() {
        commentRequestDto = CommentRequestDto.builder()
                .text("CommentRequestDtoTest")
                .build();
    }

    @Test
    void testCommentRequestDto() throws Exception {
        assertThat(validator.validate(commentRequestDto)).isEmpty();
        final JsonContent<CommentRequestDto> jsonContent = jacksonTester.write(commentRequestDto);
        assertThat(jsonContent).extractingJsonPathStringValue("$.text")
                .isEqualTo("CommentRequestDtoTest");
    }

    @Test
    void whenCommentRequestDtoTextIsNullThenViolationsNotNull() {
        assertThat(validator.validate(commentRequestDto)).isEmpty();
        commentRequestDto.setText(null);
        Set<ConstraintViolation<CommentRequestDto>> violations = validator.validate(commentRequestDto,
                Marker.OnCreate.class);
        assertThat(violations.size()).isEqualTo(2);
        violations.forEach(action -> {
            assertThat(action.getMessage())
                    .isIn(List.of("не должно равняться null", "текст комментария не может быть пустым"));
            assertThat(action.getPropertyPath().toString()).isEqualTo("text");
        });
    }

    @Test
    void whenCommentRequestDtoTextIsBlankThenViolationsNotNull() {
        assertThat(validator.validate(commentRequestDto)).isEmpty();
        commentRequestDto.setText(" ");
        Set<ConstraintViolation<CommentRequestDto>> violations = validator.validate(commentRequestDto,
                Marker.OnCreate.class);
        assertThat(violations.size()).isEqualTo(1);
        violations.forEach(action -> {
            assertThat(action.getMessage())
                    .isEqualTo("текст комментария не может быть пустым");
            assertThat(action.getPropertyPath().toString()).isEqualTo("text");
        });
    }
}
package ru.practicum.shareit.user.dto;

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
class UserRequestDtoTest {

    private final JacksonTester<UserRequestDto> jacksonTester;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private UserRequestDto userRequestDto;

    @BeforeEach
    void beforeEach() {
        userRequestDto = UserRequestDto.builder()
                .name("user")
                .email("user.info@mail.ru")
                .build();
    }

    @Test
    void testUserRequestDto() throws Exception {
        assertThat(validator.validate(userRequestDto)).isEmpty();
        final JsonContent<UserRequestDto> jsonContent = jacksonTester.write(userRequestDto);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("user");
        assertThat(jsonContent).extractingJsonPathStringValue("$.email")
                .isEqualTo("user.info@mail.ru");
    }

    @Test
    void whenUserRequestDtoNameIsNullThenViolationsNotNull() {
        assertThat(validator.validate(userRequestDto)).isEmpty();
        userRequestDto.setName(null);
        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(userRequestDto,
                Marker.OnCreate.class);
        assertThat(violations.size()).isEqualTo(1);
        violations.forEach(action -> {
            assertThat(action.getMessage())
                    .isIn(List.of("не должно равняться null"));
            assertThat(action.getPropertyPath().toString()).isEqualTo("name");
        });
    }

    @Test
    void whenUserRequestDtoEmailIsNotEmailThenViolationsNotNull() {
        assertThat(validator.validate(userRequestDto)).isEmpty();
        userRequestDto.setEmail("user.info_mail.ru");
        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(userRequestDto,
                Marker.OnCreate.class);
        assertThat(violations.size()).isEqualTo(1);
        violations.forEach(action -> {
            assertThat(action.getMessage())
                    .isIn(List.of("Адрес электронной почты не верного формата"));
            assertThat(action.getPropertyPath().toString()).isEqualTo("email");
        });
    }

    @Test
    void whenUserRequestDtoEmailIsBlankThenViolationsNotNull() {
        assertThat(validator.validate(userRequestDto)).isEmpty();
        userRequestDto.setEmail(" ");
        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(userRequestDto,
                Marker.OnCreate.class);
        assertThat(violations.size()).isEqualTo(2);
        violations.forEach(action -> {
            assertThat(action.getMessage())
                    .isIn(List.of("Адрес электронной почты не может быть пустой",
                            "Адрес электронной почты не верного формата"));
            assertThat(action.getPropertyPath().toString()).isEqualTo("email");
        });
    }

    @Test
    void whenUserRequestDtoEmailIsNullThenViolationsNotNull() {
        assertThat(validator.validate(userRequestDto)).isEmpty();
        userRequestDto.setEmail(null);
        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(userRequestDto,
                Marker.OnCreate.class);
        assertThat(violations.size()).isEqualTo(2);
        violations.forEach(action -> {
            assertThat(action.getMessage())
                    .isIn(List.of("не должно равняться null", "Адрес электронной почты не может быть пустой"));
            assertThat(action.getPropertyPath().toString()).isEqualTo("email");
        });
    }
}
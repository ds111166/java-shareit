package ru.practicum.shareit.request.dto;

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
class ItemRequestCreateDtoTest {

    private final JacksonTester<ItemRequestCreateDto> jacksonTester;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private ItemRequestCreateDto itemRequestCreateDto;

    @BeforeEach
    void setUp() {
        itemRequestCreateDto = ItemRequestCreateDto.builder()
                .description("ItemRequestCreateDto")
                .build();
    }

    @Test
    void testItemRequestCreateDto() throws Exception {
        assertThat(validator.validate(itemRequestCreateDto)).isEmpty();
        final JsonContent<ItemRequestCreateDto> jsonContent = jacksonTester.write(itemRequestCreateDto);
        assertThat(jsonContent).extractingJsonPathStringValue("$.description")
                .isEqualTo("ItemRequestCreateDto");
    }

    @Test
    void whenItemRequestCreateDtoDescriptionIsNullThenViolationsNotNull() {
        assertThat(validator.validate(itemRequestCreateDto)).isEmpty();
        itemRequestCreateDto.setDescription(null);
        Set<ConstraintViolation<ItemRequestCreateDto>> violations = validator.validate(itemRequestCreateDto,
                Marker.OnCreate.class);
        assertThat(violations.size()).isEqualTo(2);
        violations.forEach(action -> {
            assertThat(action.getMessage())
                    .isIn(List.of("не должно равняться null", "описание вещи в запросе не может быть пустым"));
            assertThat(action.getPropertyPath().toString()).isEqualTo("description");
        });
    }

    @Test
    void whenCommentRequestDtoTextIsBlankThenViolationsNotNull() {
        assertThat(validator.validate(itemRequestCreateDto)).isEmpty();
        itemRequestCreateDto.setDescription(" ");
        Set<ConstraintViolation<ItemRequestCreateDto>> violations = validator.validate(itemRequestCreateDto,
                Marker.OnCreate.class);
        assertThat(violations.size()).isEqualTo(1);
        violations.forEach(action -> {
            assertThat(action.getMessage())
                    .isEqualTo("описание вещи в запросе не может быть пустым");
            assertThat(action.getPropertyPath().toString()).isEqualTo("description");
        });
    }
}
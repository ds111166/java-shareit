package ru.practicum.shareit.item.dto;

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
class ItemCreateDtoTest {

    private final JacksonTester<ItemCreateDto> jacksonTester;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private ItemCreateDto itemCreateDto;

    @BeforeEach
    void setUp() {
        itemCreateDto = ItemCreateDto.builder()
                .name("item")
                .description("description item")
                .available(true)
                .requestId(null)
                .build();
    }

    @Test
    void testItemCreateDto() throws Exception {
        assertThat(validator.validate(itemCreateDto)).isEmpty();
        final JsonContent<ItemCreateDto> jsonContent = jacksonTester.write(itemCreateDto);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("item");
        assertThat(jsonContent).extractingJsonPathStringValue("$.description")
                .isEqualTo("description item");
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(jsonContent).extractingJsonPathNumberValue("$.requestId").isNull();
    }


    @Test
    void whenItemCreateDtoNameIsBlankThenViolationsNotNull() {
        assertThat(validator.validate(itemCreateDto)).isEmpty();
        itemCreateDto.setName(" ");
        Set<ConstraintViolation<ItemCreateDto>> violations = validator.validate(itemCreateDto,
                Marker.OnCreate.class);
        assertThat(violations.size()).isEqualTo(1);
        violations.forEach(action -> {
            assertThat(action.getMessage())
                    .isIn(List.of("Наименование вещи не может быть пустым"));
            assertThat(action.getPropertyPath().toString()).isEqualTo("name");
        });
    }

    @Test
    void whenItemCreateDtoDescriptionIsBlankThenViolationsNotNull() {
        assertThat(validator.validate(itemCreateDto)).isEmpty();
        itemCreateDto.setDescription(" ");
        Set<ConstraintViolation<ItemCreateDto>> violations = validator.validate(itemCreateDto,
                Marker.OnCreate.class);
        assertThat(violations.size()).isEqualTo(1);
        violations.forEach(action -> {
            assertThat(action.getMessage())
                    .isIn(List.of("Описание вещи не может быть пустым"));
            assertThat(action.getPropertyPath().toString()).isEqualTo("description");
        });
    }

    @Test
    void whenItemCreateDtoAvailableIsNullThenViolationsNotNull() {
        assertThat(validator.validate(itemCreateDto)).isEmpty();
        itemCreateDto.setAvailable(null);
        Set<ConstraintViolation<ItemCreateDto>> violations = validator.validate(itemCreateDto,
                Marker.OnCreate.class);
        assertThat(violations.size()).isEqualTo(1);
        violations.forEach(action -> {
            assertThat(action.getMessage())
                    .isIn(List.of("не должно равняться null"));
            assertThat(action.getPropertyPath().toString()).isEqualTo("available");
        });
    }
}
package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BookingRequestDtoTest {
    private final JacksonTester<BookingRequestDto> jacksonTester;
    private final LocalDateTime now = LocalDateTime.now();
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private BookingRequestDto bookingRequestDto;

    @BeforeEach
    void setUp() {
        bookingRequestDto = BookingRequestDto.builder()
                .itemId(2L)
                .start(LocalDateTime.of(2100, 11, 11, 11, 11))
                .end(LocalDateTime.of(2100, 12, 12, 12, 12))
                .build();
    }

    @Test
    void testJsonBookingRequestDto() throws Exception {
        assertThat(validator.validate(bookingRequestDto)).isEmpty();
        final JsonContent<BookingRequestDto> jsonContent = jacksonTester.write(bookingRequestDto);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.itemId")
                .isEqualTo(2);
        assertThat(jsonContent).extractingJsonPathStringValue("$.start")
                .isEqualTo("2100-11-11T11:11:00");
        assertThat(jsonContent).extractingJsonPathStringValue("$.end")
                .isEqualTo("2100-12-12T12:12:00");
    }

    @Test
    void whenBookingRequestDtoStartIsNullThenViolationsNotNull() {
        assertThat(validator.validate(bookingRequestDto)).isEmpty();
        bookingRequestDto.setStart(null);
        Set<ConstraintViolation<BookingRequestDto>> violations = validator.validate(bookingRequestDto);
        assertThat(violations).isNotEmpty();
        assertThat(violations.toString()).contains("не должно равняться null");
    }

    @Test
    void whenBookingRequestDtoEndIsNullThenViolationsNotNull() {
        assertThat(validator.validate(bookingRequestDto)).isEmpty();
        bookingRequestDto.setEnd(null);
        Set<ConstraintViolation<BookingRequestDto>> violations = validator.validate(bookingRequestDto);
        assertThat(violations).isNotEmpty();
        assertThat(violations.toString()).contains("не должно равняться null");
    }

    @Test
    void whenBookingRequestDtoStartIsBeforeNowThenViolationsNotNull() {
        assertThat(validator.validate(bookingRequestDto)).isEmpty();
        bookingRequestDto.setStart(now.minusSeconds(1L));
        Set<ConstraintViolation<BookingRequestDto>> violations = validator.validate(bookingRequestDto);
        assertThat(violations).isNotEmpty();
        assertThat(violations.toString()).contains("должно содержать сегодняшнее число или дату, которая еще не наступила");
    }

    @Test
    void whenBookingRequestDtoEndNotFutureThenViolationsNotNull() {
        assertThat(validator.validate(bookingRequestDto)).isEmpty();
        bookingRequestDto.setEnd(LocalDateTime.now());
        Set<ConstraintViolation<BookingRequestDto>> violations = validator.validate(bookingRequestDto);
        assertThat(violations).isNotEmpty();
        assertThat(violations.toString()).contains("должно содержать дату, которая еще не наступила");
    }

    @Test
    void whenBookingRequestDtoStartAfterEndThenViolationsNotNull() {
        assertThat(validator.validate(bookingRequestDto)).isEmpty();
        final LocalDateTime start = bookingRequestDto.getStart();
        bookingRequestDto.setStart(bookingRequestDto.getEnd());
        bookingRequestDto.setEnd(start);
        Set<ConstraintViolation<BookingRequestDto>> violations = validator.validate(bookingRequestDto);
        assertThat(violations).isNotEmpty();
        assertThat(violations.toString()).contains("{Data Booking.invalid}");
    }


}
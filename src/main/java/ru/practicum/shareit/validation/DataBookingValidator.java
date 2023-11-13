package ru.practicum.shareit.validation;

import ru.practicum.shareit.booking.dto.BookingRequestDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class DataBookingValidator implements ConstraintValidator<DataBookingValidate, BookingRequestDto> {
    @Override
    public boolean isValid(BookingRequestDto bookingRequestDto,
                           ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime start = bookingRequestDto.getStart();
        LocalDateTime end = bookingRequestDto.getEnd();
        if (start == null || end == null) {
            return false;
        }
        return start.isBefore(end);
    }
}

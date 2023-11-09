package ru.practicum.shareit.validation;

import ru.practicum.shareit.booking.dto.BookingData;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class DataBookingValidator implements ConstraintValidator<DataBookingValidate, BookingData> {
    @Override
    public boolean isValid(BookingData bookingData,
                           ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime start = bookingData.getStart();
        LocalDateTime end = bookingData.getEnd();
        if (start == null || end == null) {
            return false;
        }
        return start.isBefore(end);
    }
}

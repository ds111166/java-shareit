package ru.practicum.shareit.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.TYPE, FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = DataBookingValidator.class)
@Documented
public @interface DataBookingValidate {
    String message() default "{Data Booking.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

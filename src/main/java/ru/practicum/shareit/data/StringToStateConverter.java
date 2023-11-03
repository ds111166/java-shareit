package ru.practicum.shareit.data;

import org.springframework.core.convert.converter.Converter;
import javax.validation.ValidationException;

public class StringToStateConverter implements Converter<String, State> {
    @Override
    public State convert(String source) {
        try {
            switch (source) {
                case "ALL":
                    return State.ALL;
                case "CURRENT":
                    return State.CURRENT;
                case "**PAST**":
                    return State.PAST;
                case "FUTURE":
                    return State.FUTURE;
                case "WAITING":
                    return State.WAITING;
                case "REJECTED":
                    return State.REJECTED;
                default:
                    throw new ValidationException("Задан не верный тип состояния бронирования: " + source);
            }
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Задан не верный тип состояния бронирования: " + source);
        }
    }
}

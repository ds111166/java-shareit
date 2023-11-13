package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.data.StatusBooking;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {
    private Long id;                    // уникальный идентификатор бронирования
    private LocalDateTime start;        // дата начала бронирования
    private LocalDateTime end;          // дата конца бронирования
    private ItemResponseDto item;       // вещь, которую пользователь бронирует
    private UserResponseDto booker;     // пользователь, который осуществляет бронирование
    private StatusBooking status;       // статус бронирования
}

package ru.practicum.shareit.booking;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Builder
public class Booking {
    private Long id;              // уникальный идентификатор бронирования
    private LocalDateTime start;  // дата начала бронирования
    private LocalDateTime end;    // дата конца бронирования
    private Item item;            // вещь, которую пользователь бронирует
    private User booker;          // пользователь, который осуществляет бронирование
    private StatusBooking status; // статус бронирования

}

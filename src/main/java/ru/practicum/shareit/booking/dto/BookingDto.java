package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.data.StatusBooking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validation.Marker;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {
    private Long id;              // уникальный идентификатор бронирования
    @NotNull(groups = Marker.OnCreate.class)
    private LocalDateTime start;  // дата начала бронирования
    @NotNull(groups = Marker.OnCreate.class)
    private LocalDateTime end;    // дата конца бронирования
    @NotNull(groups = Marker.OnCreate.class)
    private ItemDto item;            // вещь, которую пользователь бронирует
    @NotNull(groups = Marker.OnCreate.class)
    private UserDto booker;          // пользователь, который осуществляет бронирование
    private StatusBooking statusId; // статус бронирования
}

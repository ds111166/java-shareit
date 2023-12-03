package ru.practicum.shareit.it.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.util.List;

@Data
@Builder
public class ItResponseDto {
    private Long id;                 // уникальный идентификатор вещи
    private String name;             // краткое название
    private String description;      // развёрнутое описание
    private Boolean available;       // статус о том, доступна или нет вещь для аренды
    private UserResponseDto owner;   // владелец вещи
    private Long requestId;      //  если вещь была создана по запросу - идентификатор соответствующего запроса
    private BookingResponseDto lastBooking;     // последнее по времени бронирования вещи
    private BookingResponseDto nextBooking;     // следующее по времени бронирование
    private List<CommentResponseDto> comments;  // комментарии
}

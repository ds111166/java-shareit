package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingBriefDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validation.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class ItemDto {
    private Long id;                 // уникальный идентификатор вещи
    @NotBlank(groups = Marker.OnCreate.class, message = "Наименование вещи не может быть пустым")
    private String name;             // краткое название
    @NotBlank(groups = Marker.OnCreate.class, message = "Описание вещи не может быть пустым")
    private String description;      // развёрнутое описание
    @NotNull(groups = Marker.OnCreate.class)
    private Boolean available;       // статус о том, доступна или нет вещь для аренды
    private UserDto owner;            // владелец вещи
    private Long itemRequestId;      //  если вещь была создана по запросу - идентификатор соответствующего запроса
    private BookingBriefDto lastBooking;  // последнее по времени бронирования вещи
    private BookingBriefDto nextBooking;  // следующее по времени бронирование
    private List<CommentDto> comments;  // комментарии
}

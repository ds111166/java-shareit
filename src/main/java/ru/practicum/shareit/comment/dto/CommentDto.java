package ru.practicum.shareit.comment.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validation.Marker;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class CommentDto {
    private Long id;                 // уникальный идентификатор комментария
    @NotNull(groups = Marker.OnCreate.class)
    private String text;             // текст комментария
    private ItemDto item;            // вещь, которую комментируют
    private UserDto author;          // пользователь - автор комментария
}

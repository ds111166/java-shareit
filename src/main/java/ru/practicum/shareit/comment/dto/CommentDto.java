package ru.practicum.shareit.comment.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.validation.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {
    private Long id;                 // уникальный идентификатор комментария
    @NotNull(groups = Marker.OnCreate.class)
    @NotBlank(groups = Marker.OnCreate.class, message = "текст комментария не может быть пустым")
    private String text;             // текст комментария
    private ItemDto item;            // вещь, которую комментируют
    private String authorName;      // имя автора комментария;
    private LocalDateTime created;  // дата создания комментария.
}

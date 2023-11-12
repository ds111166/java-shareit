package ru.practicum.shareit.comment.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentResponseDto {
    private Long id;                // уникальный идентификатор комментария
    private String text;            // текст комментария
    private ItemResponseDto item;   // вещь, которую комментируют
    private String authorName;      // имя автора комментария;
    private LocalDateTime created;  // дата создания комментария.
}

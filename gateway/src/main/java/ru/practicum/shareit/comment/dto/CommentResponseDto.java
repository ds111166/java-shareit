package ru.practicum.shareit.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.it.dto.ItResponseDto;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private Long id;                // уникальный идентификатор комментария
    private String text;            // текст комментария
    private ItResponseDto item;   // вещь, которую комментируют
    private String authorName;      // имя автора комментария;
    private LocalDateTime created;  // дата создания комментария.
}

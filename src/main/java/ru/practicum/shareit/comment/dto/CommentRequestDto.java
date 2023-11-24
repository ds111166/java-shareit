package ru.practicum.shareit.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {
    @NotNull(groups = Marker.OnCreate.class, message = "не должно равняться null")
    @NotBlank(groups = Marker.OnCreate.class, message = "текст комментария не может быть пустым")
    private String text;             // текст комментария
}

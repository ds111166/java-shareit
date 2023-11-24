package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemRequestResponseDto {
    private Long id;                        // уникальный идентификатор запроса
    private String description;             // текст запроса, содержащий описание требуемой вещи
    private UserResponseDto requestor;      // пользователь, создавший запрос
    private LocalDateTime created;          // дата и время создания запроса
    private List<ItemResponseDto> items;    // список вещей предоставленных для аренды в ответ на этот запрос
}

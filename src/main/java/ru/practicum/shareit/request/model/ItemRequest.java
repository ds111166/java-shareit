package ru.practicum.shareit.request.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@Builder
public class ItemRequest {
    private Long id;                  // уникальный идентификатор запроса
    private String description;       // текст запроса, содержащий описание требуемой вещи
    private User requester;           // пользователь, создавший запрос
    private LocalDateTime created;    // дата и время создания запроса
}
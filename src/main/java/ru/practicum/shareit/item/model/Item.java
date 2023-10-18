package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class Item {
    private Long id;                 // уникальный идентификатор вещи
    private String name;             // краткое название
    private String description;      // развёрнутое описание
    private Boolean available;       // статус о том, доступна или нет вещь для аренды
    private User owner;              // владелец вещи
    private Long itemRequestId;     //  если вещь была создана по запросу - ссылка на соответствующий запрос
}
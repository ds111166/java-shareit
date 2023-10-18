package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validation.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class ItemDto {
    private Long id;                 // уникальный идентификатор вещи
    // @NotNull(groups = Marker.OnCreate.class)
    @NotBlank(groups = Marker.OnCreate.class, message = "Наименование вещи не может быть пустым")
    private String name;             // краткое название
    // @NotNull(groups = Marker.OnCreate.class)
    @NotBlank(groups = Marker.OnCreate.class, message = "Описание вещи не может быть пустым")
    private String description;      // развёрнутое описание
    @NotNull(groups = Marker.OnCreate.class)
    private Boolean available;       // статус о том, доступна или нет вещь для аренды
    // @NotNull(groups = Marker.OnCreate.class)
    //@NotBlank(groups = Marker.OnCreate.class, message = "Владелец вещи должен быть указан")
    private UserDto owner;            // владелец вещи
    private Long itemRequestId;      //  если вещь была создана по запросу - идентификатор соответствующего запроса
}

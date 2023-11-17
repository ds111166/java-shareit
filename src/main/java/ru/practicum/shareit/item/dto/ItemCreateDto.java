package ru.practicum.shareit.item.dto;

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
public class ItemCreateDto {
    @NotBlank(groups = Marker.OnCreate.class, message = "Наименование вещи не может быть пустым")
    private String name;        // краткое название
    @NotBlank(groups = Marker.OnCreate.class, message = "Описание вещи не может быть пустым")
    private String description; // развёрнутое описание
    @NotNull(groups = Marker.OnCreate.class)
    private Boolean available;  // статус о том, доступна или нет вещь для аренды
    private Long requestId;     //  если вещь была создана по запросу - идентификатор соответствующего запроса
}

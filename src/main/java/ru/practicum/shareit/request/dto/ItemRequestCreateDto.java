package ru.practicum.shareit.request.dto;

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
public class ItemRequestCreateDto {
    @NotNull(groups = Marker.OnCreate.class,  message = "не должно равняться null")
    @NotBlank(groups = Marker.OnCreate.class, message = "описание вещи в запросе не может быть пустым")
    private String description;       // текст запроса, содержащий описание требуемой вещи
}

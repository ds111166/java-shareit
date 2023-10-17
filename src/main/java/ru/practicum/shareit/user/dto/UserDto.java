package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.validation.Marker;

import javax.validation.constraints.*;

@Data
@Builder
public class UserDto {
    private Long id;        // уникальный идентификатор пользователя
    private String name;    // имя или логин пользователя
    @NotBlank(groups = Marker.OnCreate.class, message = "Адрес электронной почты не может быть пустой")
    @Email(groups = Marker.OnCreate.class, message = "Адрес электронной почты не верного формата")
    private String email;   // адрес электронной почты
}

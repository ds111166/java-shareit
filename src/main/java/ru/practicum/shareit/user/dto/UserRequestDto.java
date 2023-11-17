package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation.Marker;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    @NotNull(groups = Marker.OnCreate.class)
    private String name;    // имя или логин пользователя
    @NotNull(groups = Marker.OnCreate.class)
    @NotBlank(groups = Marker.OnCreate.class, message = "Адрес электронной почты не может быть пустой")
    @Email(groups = Marker.OnCreate.class, message = "Адрес электронной почты не верного формата")
    private String email;   // адрес электронной почты
}

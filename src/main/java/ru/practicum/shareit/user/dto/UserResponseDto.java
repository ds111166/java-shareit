package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {
    private Long id;        // уникальный идентификатор пользователя
    private String name;    // имя или логин пользователя
    private String email;   // адрес электронной почты
}

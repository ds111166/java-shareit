package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UseRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;

@Component
public class UserMapper {
    public UserResponseDto toUserDto(User user) {

        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public User toUser(UserResponseDto userResponseDto) {

        return User.builder()
                .id(userResponseDto.getId())
                .name(userResponseDto.getName())
                .email(userResponseDto.getEmail())
                .build();
    }

    public User toUser(UseRequestDto userDto) {

        return User.builder()
                .id(null)
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }
}

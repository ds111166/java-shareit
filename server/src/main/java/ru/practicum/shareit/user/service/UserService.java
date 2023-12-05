package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.util.List;

public interface UserService {

    List<UserResponseDto> getUsers();

    UserResponseDto getUserById(Long userId);

    UserResponseDto createUser(UserRequestDto newUser);

    UserResponseDto updateUser(Long userId, UserResponseDto updatedUser);

    void deleteUser(Long userId);
}

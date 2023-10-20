package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getUsers();

    UserDto getUserById(Long userId);

    UserDto createUser(UserDto newUser);

    UserDto updateUser(Long userId, UserDto updatedUser);

    void deleteUser(Long userId);
}

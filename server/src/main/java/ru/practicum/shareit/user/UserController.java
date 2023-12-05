package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseDto> getUsers() {
        log.info("Запрос на получение списка всех пользователей");
        final List<UserResponseDto> users = userService.getUsers();
        log.info("Количество всех пользователей равно: {}", users.size());
        log.info("Идентификаторы пользователей: \"{}\"",
                users.stream().map(UserResponseDto::getId).collect(Collectors.toList()));
        return users;
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto getUserById(
            @PathVariable Long userId) {
        log.info("Запрос на получение пользователя с id: {}", userId);
        final UserResponseDto userById = userService.getUserById(userId);
        log.info("Отправлен: \"{}\"", userById);
        return userById;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(
            @RequestBody UserRequestDto newUser) {
        log.info("Запрос на создание: \"{}\"", newUser);
        final UserResponseDto user = userService.createUser(newUser);
        log.info("Создан: \"{}\"", user);
        return user;
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto updateUser(
            @PathVariable Long userId,
            @RequestBody UserResponseDto updatedUser) {
        log.info("Запрос на обновление: \"{}\"", updatedUser);
        final UserResponseDto user = userService.updateUser(userId, updatedUser);
        log.info("Обновлён: \"{}\"", user);
        return user;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(
            @PathVariable Long userId) {
        log.info("Запрос на удаление пользователя с id: {}", userId);
        userService.deleteUser(userId);
        log.info("Пользователь с id: {} удален", userId);
    }

}
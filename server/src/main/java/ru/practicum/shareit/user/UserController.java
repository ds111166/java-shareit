package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.Marker;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Validated
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
    public UserResponseDto getUserById(@PathVariable @NotNull Long userId) {

        log.info("Запрос на получение пользователя с id: {}", userId);
        final UserResponseDto userById = userService.getUserById(userId);
        log.info("Отправлен: \"{}\"", userById);
        return userById;
    }

    @PostMapping
    @Validated({Marker.OnCreate.class})
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@Valid @RequestBody UserRequestDto newUser) {

        log.info("Запрос на создание: \"{}\"", newUser);
        final UserResponseDto user = userService.createUser(newUser);
        log.info("Создан: \"{}\"", user);
        return user;
    }

    @PatchMapping("/{userId}")
    @Validated({Marker.OnUpdate.class})
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto updateUser(@PathVariable @NotNull Long userId,
                                      @Valid @RequestBody UserResponseDto updatedUser) {

        log.info("Запрос на обновление: \"{}\"", updatedUser);
        final UserResponseDto user = userService.updateUser(userId, updatedUser);
        log.info("Обновлён: \"{}\"", user);
        return user;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable @NotNull Long userId) {

        log.info("Запрос на удаление пользователя с id: {}", userId);
        userService.deleteUser(userId);
        log.info("Пользователь с id: {} удален", userId);
    }

}
package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validation.Marker;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getUsers() {
        log.info("Запрос на получение списка всех пользователей");
        final List<UserDto> users = userService.getUsers();
        log.info("Отправлен список из {} пользователей", users.size());
        return users;
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserById(@PathVariable @NotNull Long userId) {
        log.info("Запрос на получение пользователя с id: {}", userId);
        final UserDto userById = userService.getUserById(userId);
        log.info("Отправлен - {}", userById);
        return userById;
    }

    @PostMapping
    @Validated({Marker.OnCreate.class})
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody UserDto newUser) {
        log.info("Запрос на создание - {}", newUser);
        final UserDto user = userService.createUser(newUser);
        log.info("Создан - {}", user);
        return user;
    }

    @Validated({Marker.OnUpdate.class})
    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUser(@PathVariable @NotNull Long userId, @Valid @RequestBody UserDto updatedUser) {
        log.info("Запрос на обновление - {}", updatedUser);
        final UserDto user = userService.updateUser(userId, updatedUser);
        log.info("Обновлён - {}", user);
        return user;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable @NotNull Long userId) {
        log.info("Запрос на удаление пользователя с id: {}", userId);
        userService.delete(userId);
    }
}
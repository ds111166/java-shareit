package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.validation.Marker;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.info("Get users");
        return userClient.getUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable @NotNull Long userId) {
        log.info("Get users by userId={}", userId);
        return userClient.getUserById(userId);
    }

    @PostMapping
    @Validated({Marker.OnCreate.class})
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserRequestDto newUser) {
        log.info("Create user {}", newUser);
        return userClient.createUser(newUser);
    }

    @PatchMapping("/{userId}")
    @Validated({Marker.OnUpdate.class})
    public ResponseEntity<Object> updateUser(@PathVariable @NotNull Long userId,
                                             @Valid @RequestBody UserResponseDto updatedUser) {
        log.info("Update user {}, userId={}", updatedUser, userId);
        return userClient.updateUser(userId, updatedUser);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable @NotNull Long userId) {
        log.info("Delete user, userId={}", userId);
        userClient.deleteUser(userId);
    }

}
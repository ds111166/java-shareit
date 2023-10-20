package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    List<User> getUsers();

    User getUserById(Long userId);

    User createUser(User newUser);

    User updateUser(Long userId, User updateUser);

    void deleteUser(Long userId);
}

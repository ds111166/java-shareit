package ru.practicum.shareit.user.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private long generatorId = 0L;
    private final Map<Long, User> users;


    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException(String.format("Пользователя с id = %s не существует", userId));
        }
        return users.get(userId);
    }

    @Override
    public User createUser(User newUser) {
        checkingUserEmail(newUser.getEmail());
        final long userId = ++generatorId;
        newUser.setId(userId);
        users.put(userId, newUser);
        return newUser;
    }

    @Override
    public User updateUser(Long userId, User userDate) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException(String.format("Пользователя с id = %s не существует", userId));
        }
        final User updatedUser = users.get(userId);
        final Long userDateId = userDate.getId();
        if (userDateId != null && !Objects.equals(updatedUser.getId(), userDateId)) {
            throw new ConflictException("Изменять идентификатор у пользователя запрещено");
        }
        final String userDateName = userDate.getName();
        if (userDateName != null) {
            updatedUser.setName(userDateName);
        }
        final String userDateEmail = userDate.getEmail();
        if (userDateEmail != null && !Objects.equals(updatedUser.getEmail(), userDateEmail)) {
            checkingUserEmail(userDateEmail);
            updatedUser.setEmail(userDateEmail);
        }
        users.put(userId, updatedUser);
        return updatedUser;
    }

    @Override
    public void deleteUser(Long userId) {
        users.remove(userId);
    }

    private void checkingUserEmail(String userEmail) {
        final Set<String> emails = users.values().stream().map(User::getEmail).collect(Collectors.toSet());
        if (emails.contains(userEmail)) {
            throw new ConflictException(String.format("Пользователь с email: \"%s\" уже существует", userEmail));
        }
    }

}

package ru.practicum.shareit.user.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
@RequiredArgsConstructor
public class UserStorageImpl implements UserStorage {
    private long generatorId = 0L;
    private final Map<Long, User> users;
    private final Set<String> emailUniqSet;

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
        final String userEmail = newUser.getEmail();
        checkingUserEmail(userEmail);
        final long userId = ++generatorId;
        newUser.setId(userId);
        users.put(userId, newUser);
        emailUniqSet.add(userEmail);
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
        final String userEmail = updatedUser.getEmail();
        if (userDateEmail != null && !Objects.equals(userEmail, userDateEmail)) {
            checkingUserEmail(userDateEmail);
            updatedUser.setEmail(userDateEmail);
            emailUniqSet.remove(userEmail);
        }
        users.put(userId, updatedUser);
        emailUniqSet.add(userDateEmail);
        return updatedUser;
    }

    @Override
    public void deleteUser(Long userId) {
        if (users.containsKey(userId)) {
            final User user = users.get(userId);
            final String userEmail = user.getEmail();
            users.remove(userId);
            emailUniqSet.remove(userEmail);
        }
    }

    private void checkingUserEmail(String userEmail) {
        if (emailUniqSet.contains(userEmail)) {
            throw new ConflictException(String.format("Пользователь с email: \"%s\" уже существует", userEmail));
        }
    }
}

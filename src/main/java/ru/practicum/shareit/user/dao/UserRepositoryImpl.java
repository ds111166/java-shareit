package ru.practicum.shareit.user.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.item.dao.ItemRepository;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private long generatorId = 0L;
    private final Map<Long, User> users;
    private final ItemRepository itemRepository;

    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public User getUserById(Long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException(String.format("Пользователя с id = %s не существует", userId));
        }
        return users.get(userId);
    }

    public User createUser(User newUser) {
        CheckingUserEmail(newUser.getEmail());
        final long id = ++generatorId;
        newUser.setId(id);
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    public User updateUser(Long userId, User userDate) {
        if(!users.containsKey(userId)) {
            throw new NotFoundException(String.format("Пользователя с id = %s не существует", userId));
        }
        final User updatedUser = users.get(userId);
        final Long userDateId = userDate.getId();
        if(userDateId != null && !Objects.equals(updatedUser.getId(), userDateId)){
            throw new ConflictException("Попытка изменить идентификатор у существующего пользователя");
        }
        final String userDateName = userDate.getName();
        if(userDateName != null) {
            updatedUser.setName(userDateName);
        }
        final String userDateEmail = userDate.getEmail();
        if(userDateEmail != null) {
            CheckingUserEmail(userDateEmail);
            updatedUser.setEmail(userDateEmail);
        }
        users.put(userId, updatedUser);
        return updatedUser;
    }

    @Override
    public void deleteUser(Long userId) {
        if (users.containsKey(userId)) {
            itemRepository.remoteItemsByOwnerId(userId);
            users.remove(userId);
        } else {
            throw new NotFoundException(String.format("Пользователя с id = %s не существует", userId));
        }
    }

    private void CheckingUserEmail(String userEmail) {
        final Set<String> emails = users.values().stream().map(User::getEmail).collect(Collectors.toSet());
        if(emails.contains(userEmail)) {
            throw new ConflictException(String.format("Пользователь с email = %s уже существует", userEmail));
        }
    }

}

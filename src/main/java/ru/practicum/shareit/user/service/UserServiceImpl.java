package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UseRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public List<UserResponseDto> getUsers() {

        return userRepository.findAll().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponseDto getUserById(Long userId) {

        final User userById = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с id = " + userId + " не существует"));
        return userMapper.toUserDto(userById);
    }

    @Override
    @Transactional
    public UserResponseDto createUser(UseRequestDto newUser) {

        try {
            final User createdUser = userRepository.save(userMapper.toUser(newUser));
            return userMapper.toUserDto(createdUser);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException(ex.getMessage());
        }
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Long userId, UserResponseDto userData) {

        final User updateUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с id = " + userId + " не существует"));
        final String name = userData.getName();
        if (name != null) {
            updateUser.setName(name);
        }
        final String userDataEmail = userData.getEmail();
        final String userEmail = updateUser.getEmail();
        if (userDataEmail != null && !Objects.equals(userEmail, userDataEmail)) {
            checkingUserEmail(userDataEmail);
            updateUser.setEmail(userDataEmail);
        }
        try {
            final User updatedUser = userRepository.save(updateUser);
            return userMapper.toUserDto(updatedUser);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException(ex.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    private void checkingUserEmail(String userEmail) {
        if (userRepository.existsUserByEmail(userEmail)) {
            throw new ConflictException(String.format("Пользователь с email: \"%s\" уже существует", userEmail));
        }
    }
}

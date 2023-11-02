package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.ItemStorage;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dao.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    //private final UserStorage userStorage;
    private final UserRepository userRepository;
   // private final ItemStorage itemStorage;
    private final UserMapper userMapper;

    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
        /*return userStorage.getUsers().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());

         */
    }

    public UserDto getUserById(Long userId) {
        final User userById = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с id = " + userId + " не существует"));
        return userMapper.toUserDto(userById);
        /*
        final User userById = userStorage.getUserById(userId);
        return userMapper.toUserDto(userById);
         */
    }

    public UserDto createUser(UserDto newUser) {
        try {
            final String userEmail = newUser.getEmail();
            checkingUserEmail(userEmail);
            final User createdUser = userRepository.save(userMapper.toUser(newUser));
            return userMapper.toUserDto(createdUser);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException(ex.getMessage());
        }

        //final User createdUser = userStorage.createUser(userMapper.toUser(newUser));

    }

    public UserDto updateUser(Long userId, UserDto userData) {
        final User updateUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с id = " + userId + " не существует"));
        final String name = userData.getName();
        if (name != null) {
            updateUser.setName(name);
        }
        final String userDataEmail = userData.getEmail();
        final String userEmail = updateUser.getEmail();
        if(userDataEmail != null && !Objects.equals(userEmail, userDataEmail)) {
            checkingUserEmail(userDataEmail);
            updateUser.setEmail(userDataEmail);
        }
        try {
            final User updatedUser = userRepository.save(updateUser);
            return userMapper.toUserDto(updatedUser);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException(ex.getMessage());
        }

        /*
        userStorage.getUserById(userId);
        final User updatedUser = userStorage.updateUser(userId, userMapper.toUser(updateUser));
         */

    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
        //userStorage.deleteUser(userId);
        //itemStorage.remoteItemsByOwnerId(userId); //за счет каскадного удаления внешних ключей
    }
    private void checkingUserEmail(String userEmail) {
        if (userRepository.existsUserByEmail(userEmail)) {
            throw new ConflictException(String.format("Пользователь с email: \"%s\" уже существует", userEmail));
        }
    }
}

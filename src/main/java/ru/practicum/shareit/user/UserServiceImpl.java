package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final UserMapper userMapper;

    public List<UserDto> getUsers() {
        return userRepository.getUsers().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long userId) {
        return userMapper.toUserDto(userRepository.getUserById(userId));
    }

    public UserDto createUser(UserDto newUser) {
        final User createdUser = userRepository.createUser(userMapper.toUser(newUser));
        return userMapper.toUserDto(createdUser);
    }

    public UserDto updateUser(Long userId, UserDto updateUser) {
        userRepository.getUserById(userId);
        final User updatedUser = userRepository.updateUser(userId, userMapper.toUser(updateUser));
        return userMapper.toUserDto(updatedUser);
    }

    public void delete(Long userId) {
        userRepository.deleteUser(userId);
        itemRepository.remoteItemsByOwnerId(userId);
    }
}

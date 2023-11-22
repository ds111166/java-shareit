package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {
    private final EntityManager em;
    private final UserService userService;
    private final UserMapper userMapper;

    @Test
    void getUsers() {
        UserRequestDto userRequestDto1 = new UserRequestDto("user1", "user1@mail.ru");
        UserRequestDto userRequestDto2 = new UserRequestDto("user2", "user2@mail.ru");
        final UserResponseDto user1 = userService.createUser(userRequestDto1);
        final UserResponseDto user2 = userService.createUser(userRequestDto2);
        final List<UserResponseDto> users = userService.getUsers();
        assertThat(users).hasSize(2).containsAll(List.of(user1, user2));
    }

    @Test
    void getUserById() {
        final UserResponseDto userDto = userService.createUser(new UserRequestDto("user2", "user2@mail.ru"));
        final UserResponseDto userById = userService.getUserById(userDto.getId());
        assertThat(userById).isEqualTo(userDto);
        assertThrows(NotFoundException.class, () -> {
            userService.getUserById(9999L);
        });
    }

    @Test
    void createUser() {
        UserRequestDto userRequestDto = new UserRequestDto("user1", "user1@mail.ru");
        userService.createUser(userRequestDto);
        final TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = query.setParameter("email", userRequestDto.getEmail())
                .getSingleResult();
        assertThat(user.getId()).isNotNull();
        assertThat(user.getName()).isEqualTo(userRequestDto.getName());
        assertThat(user.getEmail()).isEqualTo(userRequestDto.getEmail());
    }

    @Test
    void updateUser() {
        UserRequestDto userRequestDto = new UserRequestDto("user3", "user3@mail.ru");
        UserRequestDto userRequestDto2 = new UserRequestDto("user3", "user3Plus@mail.ru");
        final UserResponseDto user = userService.createUser(userRequestDto);
        final UserResponseDto user2 = userService.createUser(userRequestDto2);
        user.setName("user3+");
        final UserResponseDto user1 = userService.updateUser(user.getId(), user);
        assertThat(user1).isEqualTo(user);
        assertThrows(NotFoundException.class, () -> {
            userService.updateUser(1234L, user);
        });
        user.setEmail(user2.getEmail());
        assertThrows(ConflictException.class, () -> {
            userService.updateUser(user.getId(), user);
        });
    }

    @Test
    void deleteUser() {
        UserRequestDto userRequestDto = new UserRequestDto("user3", "user3@mail.ru");
        final UserResponseDto user = userService.createUser(userRequestDto);
        assertThrows(EmptyResultDataAccessException.class, () -> {
            userService.deleteUser(9999L);
        });
        final UserResponseDto userById = userService.getUserById(user.getId());
        assertThat(userById).isEqualTo(user);
        userService.deleteUser(user.getId());
        assertThrows(NotFoundException.class, () -> {
            userService.getUserById(user.getId());
        });
    }
}
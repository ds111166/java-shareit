package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class UserRepositoryTest {

    private final TestEntityManager em;
    private final UserRepository userRepository;

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(em);
    }

    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {
        final List<User> users = userRepository.findAll();
        assertThat(users).isEmpty();
    }

    @Test
    void testExistsUserByEmail() {
        User user1 = em.persist(new User(null, "user1", "user1@mail.ru"));
        User user2 = em.persist(new User(null, "user2", "user2@mail.ru"));
        User user3 = em.persist(new User(null, "user3", "user3@mail.ru"));
        final List<User> users = userRepository.findAll();
        assertThat(users).hasSize(3).contains(user1, user2, user3);
        assertThat(userRepository.existsUserByEmail(user3.getEmail())).isTrue();
        assertThat(userRepository.existsUserByEmail("user@mail.ru")).isFalse();
    }
}
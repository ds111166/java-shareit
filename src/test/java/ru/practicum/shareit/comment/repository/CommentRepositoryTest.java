package ru.practicum.shareit.comment.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class CommentRepositoryTest {
    private final TestEntityManager em;
    private final CommentRepository commentRepository;
    private User user1;
    private User user2;
    private User user3;
    private User user4;
    private Item item1;

    @BeforeEach
    void setUp() {
        user1 = em.persist(new User(null, "user1", "user1@mail.ru"));
        user2 = em.persist(new User(null, "user2", "user2@mail.ru"));
        user3 = em.persist(new User(null, "user3", "user3@mail.ru"));
        user4 = em.persist(new User(null, "user4", "user4@mail.ru"));
        item1 = em.persist(new Item(null, "Перфоратор", "Перфоратор-дрель",
                true, user1, null));
    }

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(em);
    }

    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {
        final List<Comment> comments = commentRepository.findAll();
        assertThat(comments).isEmpty();
    }

    @Test
    void findAllByItemId() {
        final LocalDateTime now = LocalDateTime.now();
        Comment comment2 = new Comment(null, "ok", item1, user2, now.minusMinutes(10L));
        Comment comment3 = new Comment(null, "magnificent, wonderful just beauty",
                item1, user2, now.minusMinutes(9L));
        Comment comment4 = new Comment(null, "ok", item1, user3, now.minusMinutes(8L));
        final List<Comment> comments = commentRepository.findAll();
        assertThat(comments).isEmpty();
        final List<Comment> allByItemId = commentRepository.findAllByItemId(item1.getId());
        assertThat(allByItemId).isEmpty();
    }
}
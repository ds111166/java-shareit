package ru.practicum.shareit.request.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ItemRequestRepositoryTest {
    private final TestEntityManager em;
    private final ItemRequestRepository itemRequestRepository;

    private User user1;
    private User user2;
    private User user3;
    private User user4;


    @BeforeEach
    void setUp() {
        user1 = em.persist(new User(null, "user1", "user1@mail.ru"));
        user2 = em.persist(new User(null, "user2", "user2@mail.ru"));
        user3 = em.persist(new User(null, "user3", "user3@mail.ru"));
        user4 = em.persist(new User(null, "user4", "user4@mail.ru"));
    }

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(em);
    }

    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {
        final List<ItemRequest> itemRequests = itemRequestRepository.findAll();
        assertThat(itemRequests).isEmpty();
    }

    @Test
    void findByRequestorIdOrderByCreatedDesc() {
        final LocalDateTime now = LocalDateTime.now();
        ItemRequest itemRequest1 = em.persist(new ItemRequest(null, "Дрель", user4,
                now.minusMinutes(10L)));
        ItemRequest itemRequest2 = em.persist(new ItemRequest(null, "дрель", user1,
                now.minusMinutes(9L)));
        ItemRequest itemRequest3 = em.persist(new ItemRequest(null, "сверло", user1,
                now.minusMinutes(8L)));
        ItemRequest itemRequest4 = em.persist(new ItemRequest(null, "дрель", user2,
                now.minusMinutes(8L)));
        ItemRequest itemRequest5 = em.persist(new ItemRequest(null, "стеклорез", user4,
                now.minusMinutes(5L)));
        final List<ItemRequest> itemRequestsAll = itemRequestRepository.findAll();
        assertThat(itemRequestsAll).hasSize(5).contains(itemRequest1, itemRequest2,
                itemRequest3, itemRequest4, itemRequest5);
        final List<ItemRequest> byRequestor3 = itemRequestRepository.findByRequestorIdOrderByCreatedDesc(user3.getId());
        assertThat(byRequestor3).isEmpty();
        final List<ItemRequest> byRequestor1 = itemRequestRepository.findByRequestorIdOrderByCreatedDesc(user1.getId());
        assertThat(byRequestor1).hasSize(2).contains(itemRequest2, itemRequest3);
        assertThat(byRequestor1.get(0)).isEqualTo(itemRequest3);
        assertThat(byRequestor1.get(1)).isEqualTo(itemRequest2);
    }

    @Test
    void findByRequestorIdNot() {
        final LocalDateTime now = LocalDateTime.now();
        ItemRequest itemRequest1 = em.persist(new ItemRequest(null, "Дрель", user4,
                now.minusMinutes(10L)));
        ItemRequest itemRequest2 = em.persist(new ItemRequest(null, "дрель", user1,
                now.minusMinutes(9L)));
        ItemRequest itemRequest3 = em.persist(new ItemRequest(null, "сверло", user1,
                now.minusMinutes(8L)));
        ItemRequest itemRequest4 = em.persist(new ItemRequest(null, "дрель", user2,
                now.minusMinutes(8L)));
        ItemRequest itemRequest5 = em.persist(new ItemRequest(null, "стеклорез", user4,
                now.minusMinutes(5L)));
        final List<ItemRequest> itemRequestsAll = itemRequestRepository.findAll();
        assertThat(itemRequestsAll).hasSize(5).contains(itemRequest1, itemRequest2,
                itemRequest3, itemRequest4, itemRequest5);
        final List<ItemRequest> byRequestorIdNot1 = itemRequestRepository.findByRequestorIdNot(user1.getId(),
                null);
        assertThat(byRequestorIdNot1).hasSize(3).contains(itemRequest1, itemRequest4, itemRequest5);
    }
}
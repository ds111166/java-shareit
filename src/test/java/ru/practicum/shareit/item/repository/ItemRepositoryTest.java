package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ItemRepositoryTest {
    private final TestEntityManager em;
    //private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    //private final ItemRequestRepository itemRequestRepository;

    private User user1;
    private User user2;
    private User user3;
    private User user4;
    private ItemRequest itemRequest1;


    @BeforeEach
    void beforaEach() {
        user1 = em.persist(new User(null, "user1", "user1@mail.ru"));
        user2 = em.persist(new User(null, "user2", "user2@mail.ru"));
        user3 = em.persist(new User(null, "user3", "user3@mail.ru"));
        user4 = em.persist(new User(null, "user4", "user4@mail.ru"));
        itemRequest1 = em.persist(new ItemRequest(null, "Дрель", user4,
                LocalDateTime.now()));
    }
    @Test
    public void contextLoads() {
        Assertions.assertNotNull(em);
    }

    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {
        final List<Item> items = itemRepository.findAll();
        assertThat(items).isEmpty();
    }
    @Test
    void searchItemsByText() {

        Item item1 = em.persist(new Item(null, "Перфоратор", "Перфоратор-дрель",
                true, user1, itemRequest1.getId() ));
        Item item2 = em.persist(new Item(null, "Нож", "нож кухонный",
                true, user1, null ));
        Item item3 = em.persist(new Item(null, "лом", "лом стальной",
                true, user1, null ));
        Item item4 = em.persist(new Item(null, "проектор", "проектор лазерный",
                true, user2, null ));
        Item item5 = em.persist(new Item(null, "дрель", "дрель, шуруповерт",
                true, user2, null ));
        Item item6 = em.persist(new Item(null, "сверла", "сверла по металлу",
                true, user3, itemRequest1.getId() ));
        final List<Item> items = itemRepository.findAll();
        assertThat(items).hasSize(6).contains(item1, item2, item3,item4,item5,item6);
    }

    @Test
    void findByOwnerId() {
    }

    @Test
    void findByRequestId() {
    }
}
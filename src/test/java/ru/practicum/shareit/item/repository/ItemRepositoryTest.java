package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ItemRepositoryTest {
    private final TestEntityManager em;
    private final ItemRepository itemRepository;

    private User user1;
    private User user2;
    private User user3;
    private User user4;
    private ItemRequest itemRequest1;


    @BeforeEach
    void setUp() {
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
                true, user1, itemRequest1.getId()));
        Item item2 = em.persist(new Item(null, "Нож", "нож кухонный",
                true, user1, null));
        Item item3 = em.persist(new Item(null, "лом", "лом стальной",
                true, user1, null));
        Item item4 = em.persist(new Item(null, "проектор", "проектор лазерный",
                true, user2, null));
        Item item5 = em.persist(new Item(null, "дрель", "дрель, шуруповерт",
                true, user2, null));
        Item item6 = em.persist(new Item(null, "сверла", "сверла по металлу",
                true, user3, itemRequest1.getId()));
        final List<Item> items = itemRepository.findAll();
        assertThat(items).hasSize(6).contains(item1, item2, item3, item4, item5, item6);
        final List<Item> items1 = itemRepository.searchItemsByText("рел", null);
        assertThat(items1).hasSize(2).contains(item1, item5);
    }

    @Test
    void findByOwnerId() {
        Item item1 = em.persist(new Item(null, "Перфоратор", "Перфоратор-дрель",
                true, user1, itemRequest1.getId()));
        Item item2 = em.persist(new Item(null, "Нож", "нож кухонный",
                true, user1, null));
        Item item3 = em.persist(new Item(null, "лом", "лом стальной",
                true, user1, null));
        Item item4 = em.persist(new Item(null, "проектор", "проектор лазерный",
                true, user2, null));
        Item item5 = em.persist(new Item(null, "дрель", "дрель, шуруповерт",
                true, user2, null));
        Item item6 = em.persist(new Item(null, "сверла", "сверла по металлу",
                true, user3, itemRequest1.getId()));
        final List<Item> items = itemRepository.findAll();
        assertThat(items).hasSize(6).contains(item1, item2, item3, item4, item5, item6);
        final List<Item> itemsByOwnerId1 = itemRepository.findByOwnerId(user1.getId(), null);
        assertThat(itemsByOwnerId1).hasSize(3).contains(item1, item2, item3);
        final List<Item> itemsByOwnerId3 = itemRepository.findByOwnerId(user3.getId(), null);
        assertThat(itemsByOwnerId3).hasSize(1).contains(item6);
        final List<Item> itemsByOwnerId4 = itemRepository.findByOwnerId(user4.getId(), null);
        assertThat(itemsByOwnerId4).isEmpty();
    }

    @Test
    void findByRequestId() {
        Item item1 = em.persist(new Item(null, "Перфоратор", "Перфоратор-дрель",
                true, user1, itemRequest1.getId()));
        Item item2 = em.persist(new Item(null, "Нож", "нож кухонный",
                true, user1, null));
        Item item3 = em.persist(new Item(null, "лом", "лом стальной",
                true, user1, null));
        Item item4 = em.persist(new Item(null, "проектор", "проектор лазерный",
                true, user2, null));
        Item item5 = em.persist(new Item(null, "дрель", "дрель, шуруповерт",
                true, user2, null));
        Item item6 = em.persist(new Item(null, "сверла", "сверла по металлу",
                true, user3, itemRequest1.getId()));
        final List<Item> items = itemRepository.findAll();
        assertThat(items).hasSize(6).contains(item1, item2, item3, item4, item5, item6);
        final List<Item> itemsByRequestId = itemRepository.findByRequestId(itemRequest1.getId(), null);
        assertThat(itemsByRequestId).hasSize(2).contains(item1, item6);
    }
}
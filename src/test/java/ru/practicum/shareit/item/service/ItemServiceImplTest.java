package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

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
class ItemServiceImplTest {

    private final EntityManager em;
    private final ItemService itemService;
    private final UserService userService;
    private final ItemRequestService itemRequestService;
    private final ItemMapper itemMapper;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getOwnerItems() {
    }

    @Test
    void getItemById() {
    }

    @Test
    void createItem() {
        ItemCreateDto itemCreateDto = new ItemCreateDto("item", "des_item", true, null);
        UserRequestDto userRequestDto = new UserRequestDto("user1", "user1@mail.ru");
        final UserResponseDto user1 = userService.createUser(userRequestDto);
        final ItemResponseDto createdItem = itemService.createItem(user1.getId(), itemCreateDto);
        final TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = :id", Item.class);
        Item item = query.setParameter("id", createdItem.getId()).getSingleResult();
        final ItemResponseDto itemDto = itemMapper.toItemDto(item);
        assertThat(createdItem).isEqualTo(itemDto);
    }

    @Test
    void updateItem() {
    }

    @Test
    void searchItemsByText() {
        ItemCreateDto itemCreateDto1 = new ItemCreateDto("дрель", "Дрель bosh", true, null);
        ItemCreateDto itemCreateDto2 = new ItemCreateDto("шуруповерт", "шуруповерт, дрель", true, null);
        UserRequestDto userRequestDto = new UserRequestDto("user1", "user1@mail.ru");
        final UserResponseDto user1 = userService.createUser(userRequestDto);
        final ItemResponseDto createdItem1 = itemService.createItem(user1.getId(), itemCreateDto1);
        final ItemResponseDto createdItem2 = itemService.createItem(user1.getId(), itemCreateDto2);
        final List<ItemResponseDto> items = itemService.searchItemsByText("дрель", 0, 1000);
        assertThat(items).hasSize(2).containsAll(List.of(createdItem1,createdItem2));
        final List<ItemResponseDto> items2 = itemService.searchItemsByText("дрель", -1, 1000);
    }

    @Test
    void createComment() {
    }

    @Test
    void findByItemRequestId() {
    }
}
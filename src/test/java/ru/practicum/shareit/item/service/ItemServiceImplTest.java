package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

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

    @Test
    void getOwnerItems() {
        ItemCreateDto itemCreateDto1 = new ItemCreateDto("item1", "des_item1",
                true, null);
        ItemCreateDto itemCreateDto2 = new ItemCreateDto("item2", "des_item2",
                true, null);
        ItemCreateDto itemCreateDto3 = new ItemCreateDto("item3", "des_item3",
                true, null);
        ItemCreateDto itemCreateDto4 = new ItemCreateDto("item4", "des_item4",
                true, null);
        final UserResponseDto user1 = userService.createUser(new UserRequestDto("user1",
                "user1@mail.ru"));
        final UserResponseDto user2 = userService.createUser(new UserRequestDto("user2",
                "user2@mail.ru"));
        final ItemResponseDto item1 = itemService.createItem(user1.getId(), itemCreateDto1);
        final ItemResponseDto item2 = itemService.createItem(user2.getId(), itemCreateDto2);
        final ItemResponseDto item3 = itemService.createItem(user1.getId(), itemCreateDto3);
        final ItemResponseDto item4 = itemService.createItem(user1.getId(), itemCreateDto4);
        List<ItemResponseDto> itemsByOwner2 = itemService.getOwnerItems(user2.getId(), 0, 100);
        assertThat(itemsByOwner2).hasSize(1);
        assertThat(itemsByOwner2.get(0).getId()).isEqualTo(item2.getId());
        assertThrows(NotFoundException.class, () -> {
            itemService.getOwnerItems(12345L, 0, 100);
        });
    }

    @Test
    void getItemById() {
        ItemCreateDto itemCreateDto = new ItemCreateDto("item",
                "des_item", true, null);
        UserRequestDto userRequestDto = new UserRequestDto("user1", "user1@mail.ru");
        final UserResponseDto user1 = userService.createUser(userRequestDto);
        final ItemResponseDto createdItem = itemService.createItem(user1.getId(), itemCreateDto);
        ItemResponseDto itemById = itemService.getItemById(user1.getId(), createdItem.getId());
        assertThat(itemById.getId()).isEqualTo(createdItem.getId());
        assertThat(itemById.getName()).isEqualTo(createdItem.getName());
        assertThat(itemById.getDescription()).isEqualTo(createdItem.getDescription());
        assertThrows(NotFoundException.class, () -> {
            itemService.getItemById(user1.getId(), 9999L);
        });
    }

    @Test
    void createItem() {
        ItemCreateDto itemCreateDto = new ItemCreateDto("item",
                "des_item", true, null);
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
        ItemCreateDto itemCreateDto = new ItemCreateDto("item",
                "des_item", true, null);
        UserRequestDto userRequestDto = new UserRequestDto("user1", "user1@mail.ru");
        final UserResponseDto user1 = userService.createUser(userRequestDto);
        final ItemResponseDto createdItem = itemService.createItem(user1.getId(), itemCreateDto);
        createdItem.setDescription("des_item_update");
        ItemResponseDto updatedItem = itemService.updateItem(user1.getId(), createdItem.getId(), createdItem);
        assertThat(updatedItem.getId()).isEqualTo(createdItem.getId());
        assertThat(updatedItem.getDescription()).isEqualTo("des_item_update");
    }

    @Test
    void searchItemsByText() {
        ItemCreateDto itemCreateDto1 = new ItemCreateDto("дрель",
                "Дрель bosh", true, null);
        ItemCreateDto itemCreateDto2 = new ItemCreateDto("шуруповерт",
                "шуруповерт, дрель", true, null);
        UserRequestDto userRequestDto = new UserRequestDto("user1", "user1@mail.ru");
        final UserResponseDto user1 = userService.createUser(userRequestDto);
        final ItemResponseDto createdItem1 = itemService.createItem(user1.getId(), itemCreateDto1);
        final ItemResponseDto createdItem2 = itemService.createItem(user1.getId(), itemCreateDto2);
        final List<ItemResponseDto> items = itemService.searchItemsByText("дрель", 0, 1000);
        assertThat(items).hasSize(2).containsAll(List.of(createdItem1, createdItem2));
    }

    @Test
    void createComment() {
        ItemCreateDto itemCreateDto1 = new ItemCreateDto("шуруповерт",
                "шуруповерт, дрель", true, null);
        UserRequestDto userRequestDto = new UserRequestDto("user1", "user1@mail.ru");
        final UserResponseDto user1 = userService.createUser(userRequestDto);
        final ItemResponseDto createdItem1 = itemService.createItem(user1.getId(), itemCreateDto1);
        assertThrows(ValidationException.class, () -> {
            itemService.createComment(user1.getId(),
                    createdItem1.getId(), new CommentRequestDto("красота"));
        });
    }

    @Test
    void findByItemRequestId() {

        final UserResponseDto requestor12 = userService
                .createUser(new UserRequestDto("requestor", "requestor@mail.ru"));
        ItemRequestResponseDto itemRequest = itemRequestService.
                createItemRequest(requestor12.getId(), new ItemRequestCreateDto("дрель"));
        ItemCreateDto itemCreateDto1 = new ItemCreateDto("шуруповерт",
                "шуруповерт, дрель", true, itemRequest.getId());

        final UserResponseDto user1 = userService.createUser(new UserRequestDto("user11", "user11@mail.ru"));
        final ItemResponseDto createdItem1 = itemService.createItem(user1.getId(), itemCreateDto1);
        List<Long> itemIdsByRequestId = itemService.findByItemRequestId(itemRequest.getId())
                .stream()
                .map(ItemResponseDto::getId)
                .collect(Collectors.toList());
        assertThat(itemIdsByRequestId).hasSize(1).contains(createdItem1.getId());
    }
}
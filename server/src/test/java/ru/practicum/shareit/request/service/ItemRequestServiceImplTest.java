package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceImplTest {

    private final EntityManager em;
    private final UserService userService;
    private final ItemRequestService itemRequestService;
    private final ItemRequestMapper itemRequestMapper;

    @Test
    void createItemRequest() {
        final UserResponseDto requestor = userService
                .createUser(new UserRequestDto("requestor", "requestor@mail.ru"));
        ItemRequestResponseDto createdItemRequest = itemRequestService
                .createItemRequest(requestor.getId(), new ItemRequestCreateDto("дрель"));
        final TypedQuery<ItemRequest> query = em.createQuery("Select ir from ItemRequest ir where ir.id = :id",
                ItemRequest.class);
        ItemRequest itemRequest = query.setParameter("id", createdItemRequest.getId()).getSingleResult();
        ItemRequestResponseDto itemRequestResponseDto = itemRequestMapper
                .toItemRequestResponseDto(itemRequest, requestor);
        assertThat(itemRequestResponseDto.getId()).isEqualTo(createdItemRequest.getId());
        assertThat(itemRequestResponseDto.getRequestor()).isEqualTo(createdItemRequest.getRequestor());
        assertThat(itemRequestResponseDto.getCreated()).isEqualTo(createdItemRequest.getCreated());
    }

    @Test
    void getItemRequestById() {
        final UserResponseDto requestor = userService
                .createUser(new UserRequestDto("requestor", "requestor@mail.ru"));
        ItemRequestResponseDto createdItemRequest = itemRequestService
                .createItemRequest(requestor.getId(), new ItemRequestCreateDto("дрель"));
        ItemRequestResponseDto itemRequestById = itemRequestService
                .getItemRequestById(requestor.getId(), createdItemRequest.getId());
        assertThat(itemRequestById).isEqualTo(createdItemRequest);
    }

    @Test
    void getItemRequests() {
        final UserResponseDto requestor = userService
                .createUser(new UserRequestDto("requestor", "requestor@mail.ru"));
        ItemRequestResponseDto createdItemRequest = itemRequestService
                .createItemRequest(requestor.getId(), new ItemRequestCreateDto("дрель"));
        List<ItemRequestResponseDto> itemRequests = itemRequestService.getItemRequests(requestor.getId());
        assertThat(itemRequests).hasSize(1).contains(createdItemRequest);
    }

    @Test
    void getItemRequestsAll() {
        final UserResponseDto requestor1 = userService
                .createUser(new UserRequestDto("requestor1", "requestor1@mail.ru"));
        final UserResponseDto requestor2 = userService
                .createUser(new UserRequestDto("requestor2", "requestor2@mail.ru"));

        ItemRequestResponseDto itemRequest1 = itemRequestService
                .createItemRequest(requestor1.getId(), new ItemRequestCreateDto("дрель"));
        ItemRequestResponseDto itemRequest21 = itemRequestService
                .createItemRequest(requestor2.getId(), new ItemRequestCreateDto("дрель"));
        ItemRequestResponseDto itemRequest22 = itemRequestService
                .createItemRequest(requestor2.getId(), new ItemRequestCreateDto("дрель"));
        List<ItemRequestResponseDto> itemRequestsAll = itemRequestService
                .getItemRequestsAll(requestor1.getId(), 0, 100);
        assertThat(itemRequestsAll).hasSize(2).containsAll(List.of(itemRequest21, itemRequest22));
    }
}
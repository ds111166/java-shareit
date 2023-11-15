package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final UserService userService;
    private final UserMapper userMapper;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;


    @Override
    @Transactional
    public ItemRequestResponseDto createItemRequest(Long requestorId, ItemRequestCreateDto newItemRequest) {
        final User requestor = userMapper.toUser(userService.getUserById(requestorId));
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(newItemRequest, requestor);
        ItemRequest createdItemRequest = itemRequestRepository.save(itemRequest);
        final UserResponseDto userDto = userMapper.toUserDto(createdItemRequest.getRequester());
        return itemRequestMapper.toItemRequestResponseDto(createdItemRequest, userDto);
    }

    @Override
    @Transactional
    public ItemRequestResponseDto getItemRequestById(Long userId, Long requestId) {
        userService.getUserById(userId);
        final ItemRequest  itemRequestById = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запроса вещи с id = " + requestId + " не существует"));
        List<ItemResponseDto> itemsDto = itemRepository.findByRequestId(requestId);
        final UserResponseDto userDto = userMapper.toUserDto(itemRequestById.getRequester());
        return itemRequestMapper.toItemRequestResponseDto(itemRequestById, userDto, itemsDto);
    }

    @Override
    @Transactional
    public List<ItemRequestResponseDto> getItemRequests(Long requestorId) {
        userMapper.toUser(userService.getUserById(requestorId));
        final List<ItemRequest> itemRequests =  itemRequestRepository
                .findByRequestorIdOrderByCreatedDesc(requestorId);
        return itemRequests
                .stream()
                .map(itemRequest ->itemRequestMapper.toItemRequestResponseDto(itemRequest,
                        userMapper.toUserDto(itemRequest.getRequester()),
                        itemRepository.findByRequestId(itemRequest.getRequester().getId())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<ItemRequestResponseDto> getItemRequestsAll(Long userId, Integer from, Integer size) {
        userService.getUserById(userId);
        if(size == 0) {
            return new ArrayList<>();
        }
        Pageable sortedByCreated = PageRequest.of(from / size, size, Sort.by("created").descending());
        final List<ItemRequest> itemRequests =  itemRequestRepository
                .findByRequestorIdNot(userId, sortedByCreated);

        return itemRequests
                .stream()
                .map(itemRequest ->itemRequestMapper.toItemRequestResponseDto(itemRequest,
                        userMapper.toUserDto(itemRequest.getRequester()),
                        itemRepository.findByRequestId(itemRequest.getRequester().getId())))
                .collect(Collectors.toList());
    }
}

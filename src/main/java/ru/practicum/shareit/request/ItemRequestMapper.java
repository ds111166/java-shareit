package ru.practicum.shareit.request;

import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRequestMapper {

    public ItemRequest toItemRequest(ItemRequestCreateDto newItemRequest, User requestor) {
        return ItemRequest.builder()
                .id(null)
                .description(newItemRequest.getDescription())
                .requester(requestor)
                .created(LocalDateTime.now())
                .build();
    }

    public ItemRequestResponseDto toItemRequestResponseDto(ItemRequest itemRequest, UserResponseDto requestor) {
        return ItemRequestResponseDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestor(requestor)
                .created(itemRequest.getCreated())
                .build();
    }

    public ItemRequestResponseDto toItemRequestResponseDto(ItemRequest itemRequest, UserResponseDto requestor,
                                                           List<ItemResponseDto> items) {
        return ItemRequestResponseDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestor(requestor)
                .created(itemRequest.getCreated())
                .items(items)
                .build();
    }
}

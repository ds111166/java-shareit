package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

public interface ItemRequestService {
    ItemRequestResponseDto createItemRequest(Long requestorId, ItemRequestCreateDto newItemRequest);

    ItemRequestResponseDto getItemRequestById(Long userId, Long requestId);

}

package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import javax.validation.constraints.Min;
import java.util.List;

public interface ItemRequestService {
    ItemRequestResponseDto createItemRequest(Long requestorId, ItemRequestCreateDto newItemRequest);

    ItemRequestResponseDto getItemRequestById(Long userId, Long requestId);

    List<ItemRequestResponseDto> getItemRequests(Long requestorId);

    List<ItemRequestResponseDto> getItemRequestsAll(Long userId, @Min(0) Integer from, @Min(1) Integer size);

}

package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import javax.validation.constraints.Min;
import java.util.List;

public interface ItemService {

    List<ItemResponseDto> getOwnerItems(Long ownerId, @Min(0) Integer from, @Min(0) Integer size);

    ItemResponseDto getItemById(Long userId, Long itemId);

    ItemResponseDto createItem(Long ownerId, ItemCreateDto newItem);

    ItemResponseDto updateItem(Long ownerId, Long itemId, ItemResponseDto itemData);

    List<ItemResponseDto> searchItemsByText(String text, @Min(0) Integer from, @Min(0) Integer size);

    CommentResponseDto createComment(Long authorId, Long itemId, CommentRequestDto newComment);

    List<ItemResponseDto> findByItemRequestId(Long requestId);

}

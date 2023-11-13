package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.util.List;

public interface ItemService {

    List<ItemResponseDto> getOwnerItems(Long ownerId);

    ItemResponseDto getItemById(Long userId, Long itemId);

    ItemResponseDto createItem(Long ownerId, ItemCreateDto newItem);

    ItemResponseDto updateItem(Long ownerId, Long itemId, ItemResponseDto itemData);

    List<ItemResponseDto> searchItemsByText(String text);

    CommentResponseDto createComment(Long authorId, Long itemId, CommentRequestDto newComment);

}

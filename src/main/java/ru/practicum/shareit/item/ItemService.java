package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    List<ItemDto> getOwnerItems(Long ownerId);

    ItemDto getItemById(Long itemId);

    ItemDto createItem(Long ownerId, ItemDto newItem);

    ItemDto updateItem(Long ownerId, Long itemId, ItemDto itemData);

    List<ItemDto> searchItemsByText(String text);
}

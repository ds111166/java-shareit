package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {

    List<Item> getOwnerItems(Long ownerId);

    Item getItemById(Long itemId);

    void remoteItemsByOwnerId(Long userId);

    Item createItem(Item newItem);

    Item updateItem(Long ownerId, Long itemId, ItemDto itemData);

    List<Item> searchItemsByText(String text);

}

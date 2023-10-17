package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    List<Item> getItems();

    Item getItemById(Long itemId);

    Item createItem(Item newItem);

    Item updateItem(Long itemId, Item updateItem);

    void deleteItem(Long itemId);

    void remoteItemsByOwnerId(Long userId);
}

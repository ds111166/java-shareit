package ru.practicum.shareit.item.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private long generatorId = 0L;
    private final Map<Long, Item> items;


    @Override
    public List<Item> getItems() {
        return new ArrayList<>(items.values());
    }

    @Override
    public Item getItemById(Long itemId) {
        if (!items.containsKey(itemId)) {
            throw new NotFoundException(String.format("Вещи с id = %s не существует", itemId));
        }
        return items.get(itemId);
    }

    @Override
    public Item createItem(Item newItem) {
        return null;
    }

    @Override
    public Item updateItem(Long itemId, Item updateItem) {
        return null;
    }

    @Override
    public void deleteItem(Long itemId) {

    }

    @Override
    public void remoteItemsByOwnerId(Long userId) {

    }
}

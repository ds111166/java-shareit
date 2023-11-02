package ru.practicum.shareit.item.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemStorageImpl implements ItemStorage {
    private long generatorId = 0L;
    private final Map<Long, Item> items;


    @Override
    public List<Item> getOwnerItems(Long ownerId) {
        return items.values().stream()
                .filter(item -> Objects.equals(item.getOwner().getId(), ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public Item getItemById(Long itemId) {
        if (!items.containsKey(itemId)) {
            throw new NotFoundException(String.format("Вещи с id = %s не существует", itemId));
        }
        return items.get(itemId);
    }

    @Override
    public void remoteItemsByOwnerId(Long userId) {
        items.entrySet()
                .removeIf(entry -> Objects.equals(entry.getValue().getOwner().getId(), userId));
    }

    @Override
    public Item createItem(Item newItem) {
        final long itemId = ++generatorId;
        newItem.setId(itemId);
        items.put(itemId, newItem);
        return newItem;
    }

    @Override
    public Item updateItem(Long ownerId, Long itemId, ItemDto itemData) {
        if (!items.containsKey(itemId)) {
            throw new NotFoundException(String.format("Вещь с id = %s не существует", itemId));
        }
        final Item updatedItem = items.get(itemId);
        if (!Objects.equals(updatedItem.getOwner().getId(), ownerId)) {
            throw new ForbiddenException(String.format("Пользователь с id = %s не является владельцем вещи с id = %s",
                    ownerId, updatedItem.getOwner().getId()));
        }
        final Long itemDateId = itemData.getId();
        if (itemDateId != null && !Objects.equals(updatedItem.getId(), itemDateId)) {
            throw new ConflictException("Изменять идентификатор у вещи запрещено");
        }
        if (itemData.getName() != null) {
            updatedItem.setName(itemData.getName());
        }
        if (itemData.getDescription() != null) {
            updatedItem.setDescription(itemData.getDescription());
        }
        if (itemData.getAvailable() != null) {
            updatedItem.setAvailable(itemData.getAvailable());
        }
        items.put(itemId, updatedItem);
        return updatedItem;
    }

    @Override
    public List<Item> searchItemsByText(String text) {
        return items.values().stream()
                .filter(item -> !text.isEmpty())
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }
}

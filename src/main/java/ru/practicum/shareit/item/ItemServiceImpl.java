package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserService userService;

    @Override
    @Transactional
    public List<ItemDto> getOwnerItems(Long ownerId) {
        return itemRepository.findByOwnerId(ownerId)
                .stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ItemDto getItemById(Long itemId) {
        final Item itemById = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещи с id = " + itemId + " не существует"));
        return itemMapper.toItemDto(itemById);
    }

    @Override
    @Transactional
    public ItemDto createItem(Long ownerId, ItemDto newItemDto) {
        final UserDto owner = userService.getUserById(ownerId);
        try {
            final Item createdItem = itemRepository.save(itemMapper.toItem(newItemDto, owner));
            return itemMapper.toItemDto(createdItem);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException(ex.getMessage());
        }
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long ownerId, Long itemId, ItemDto itemData) {
        userService.getUserById(ownerId);
        final Item updateItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещи с id = " + itemId + " не существует"));
        if (!Objects.equals(updateItem.getOwner().getId(), ownerId)) {
            throw new ForbiddenException(String.format("Пользователь с id = %s не является владельцем вещи с id = %s",
                    ownerId, updateItem.getOwner().getId()));
        }
        final Long itemDateId = itemData.getId();
        if (itemDateId != null && !Objects.equals(updateItem.getId(), itemDateId)) {
            throw new ConflictException("Изменять идентификатор у вещи запрещено");
        }
        if (itemData.getName() != null) {
            updateItem.setName(itemData.getName());
        }
        if (itemData.getDescription() != null) {
            updateItem.setDescription(itemData.getDescription());
        }
        if (itemData.getAvailable() != null) {
            updateItem.setAvailable(itemData.getAvailable());
        }
        try {
            final Item updatedItem = itemRepository.save(updateItem);
            return itemMapper.toItemDto(updatedItem);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException(ex.getMessage());
        }
    }

    @Override
    @Transactional
    public List<ItemDto> searchItemsByText(String text) {
        if (text == null || text.isEmpty() || text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.searchItemsByText(text)
                .stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}

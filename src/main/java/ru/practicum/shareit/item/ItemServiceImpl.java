package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dao.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    //private final ItemStorage itemStorage;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserService userService;

    @Override
    public List<ItemDto> getOwnerItems(Long ownerId) {
        return itemRepository.findByOwnerId(ownerId)
                .stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
        /*
        return itemStorage.getOwnerItems(ownerId)
                .stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
         */

    }

    @Override
    public ItemDto getItemById(Long itemId) {
        final Item itemById = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещи с id = " + itemId + " не существует"));
        //final Item itemById = itemStorage.getItemById(itemId);
        return itemMapper.toItemDto(itemById);
    }

    @Override
    public ItemDto createItem(Long ownerId, ItemDto newItemDto) {
        final UserDto owner = userService.getUserById(ownerId);
        try {
            final Item createdItem = itemRepository.save(itemMapper.toItem(newItemDto, owner));
            return itemMapper.toItemDto(createdItem);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException(ex.getCause());
        }
        /*
        final Item createdItem = itemStorage.createItem(itemMapper.toItem(newItemDto, owner));
        return itemMapper.toItemDto(createdItem);

         */
    }

    @Override
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
        if(itemData.getName() != null) {
            updateItem.setName(itemData.getName());
        }
        if (itemData.getDescription() != null) {
            updateItem.setDescription(itemData.getDescription());
        }
        if (itemData.getAvailable() != null) {
            updateItem.setAvailable(itemData.getAvailable());
        }
        try {
            final Item updatedItem =  itemRepository.save(updateItem);
            return itemMapper.toItemDto(updatedItem);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException(ex.getCause());
        }
        /*
        final Item updatedItem = itemStorage.updateItem(ownerId, itemId, itemData);
        return itemMapper.toItemDto(updatedItem);
         */
    }

    @Override
    public List<ItemDto> searchItemsByText(String text) {
        return itemRepository.searchItemsByText(text)
                .stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}

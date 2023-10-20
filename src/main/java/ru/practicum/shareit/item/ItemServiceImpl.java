package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserService userService;

    @Override
    public List<ItemDto> getOwnerItems(Long ownerId) {
        return itemRepository.getOwnerItems(ownerId)
                .stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        final Item itemById = itemRepository.getItemById(itemId);
        return itemMapper.toItemDto(itemById);
    }

    @Override
    public ItemDto createItem(Long ownerId, ItemDto newItemDto) {
        final UserDto owner = userService.getUserById(ownerId);
        final Item createdItem = itemRepository.createItem(itemMapper.toItem(newItemDto, owner));
        return itemMapper.toItemDto(createdItem);
    }

    @Override
    public ItemDto updateItem(Long ownerId, Long itemId, ItemDto itemData) {
        userService.getUserById(ownerId);
        final Item updatedItem = itemRepository.updateItem(ownerId, itemId, itemData);
        return itemMapper.toItemDto(updatedItem);
    }

    @Override
    public List<ItemDto> searchItemsByText(String text) {
        return itemRepository.searchItemsByText(text)
                .stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}

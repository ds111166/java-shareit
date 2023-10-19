package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
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
        return itemMapper.toItemDto(itemRepository.getItemById(itemId));
    }

    @Override
    public ItemDto createItem(Long ownerId, ItemDto newItemDto) {
        final UserDto owner = userService.getUserById(ownerId);
        newItemDto.setOwner(owner);
        return itemMapper.toItemDto(itemRepository.createItem(itemMapper.toItem(newItemDto)));
    }

    @Override
    public ItemDto updateItem(Long ownerId, Long itemId, ItemDto itemData) {
        userService.getUserById(ownerId);
        return itemMapper.toItemDto(itemRepository.updateItem(ownerId, itemId, itemData));
    }

    @Override
    public List<ItemDto> searchItemsByText(String text) {
        return itemRepository.searchItemsByText(text)
                .stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}

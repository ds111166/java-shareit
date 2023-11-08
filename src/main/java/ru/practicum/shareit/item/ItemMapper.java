package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingBriefDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemMapper {

    private final UserMapper userMapper;

    public ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(null)
                .itemRequestId(item.getItemRequestId())
                .build();
    }

    public Item toItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(userMapper.toUser(itemDto.getOwner()))
                .itemRequestId(itemDto.getItemRequestId()).build();
    }

    public Item toItem(ItemDto itemDto, UserDto owner) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(userMapper.toUser(owner))
                .itemRequestId(itemDto.getItemRequestId()).build();
    }

    public ItemDto toItemDto(Item item, BookingBriefDto bookingDtoLast, BookingBriefDto bookingDtoNext) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(null)
                .itemRequestId(item.getItemRequestId())
                .lastBooking(bookingDtoLast)
                .nextBooking(bookingDtoNext)
                .build();
    }

    public ItemDto toItemDto(Item item, BookingBriefDto bookingDtoLast, BookingBriefDto bookingDtoNext, List<Comment> comments) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(userMapper.toUserDto(item.getOwner()))
                .itemRequestId(item.getItemRequestId())
                .lastBooking(bookingDtoLast)
                .nextBooking(bookingDtoNext)
                .comments(new ArrayList<>(comments))
                .build();
    }
}

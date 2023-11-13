package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemMapper {

    public ItemResponseDto toItemDto(Item item) {

        return ItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(null)
                .itemRequestId(item.getItemRequestId())
                .build();
    }

    public Item toItem(ItemResponseDto itemResponseDto, User owner) {

        return Item.builder()
                .id(itemResponseDto.getId())
                .name(itemResponseDto.getName())
                .description(itemResponseDto.getDescription())
                .available(itemResponseDto.getAvailable())
                .owner(owner)
                .itemRequestId(itemResponseDto.getItemRequestId())
                .build();
    }

    public Item toItem(ItemCreateDto itemDto, User owner) {

        return Item.builder()
                .id(null)
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(owner)
                .build();
    }

    public ItemResponseDto toItemDto(Item item,
                                     UserResponseDto owner,
                                     BookingResponseDto bookingDtoLast,
                                     BookingResponseDto bookingDtoNext,
                                     List<CommentResponseDto> comments) {

        return ItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(owner)
                .itemRequestId(item.getItemRequestId())
                .lastBooking(bookingDtoLast)
                .nextBooking(bookingDtoNext)
                .comments(comments)
                .build();
    }
}

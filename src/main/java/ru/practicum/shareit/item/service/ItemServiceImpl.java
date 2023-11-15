package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.booking.data.StatusBooking;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public List<ItemResponseDto> getOwnerItems(Long ownerId, Integer from, Integer size) {
        userService.getUserById(ownerId);
        if(size == 0) {
            return new ArrayList<>();
        }
        Pageable sortedById = PageRequest.of(from / size, size, Sort.by("id").ascending());
        List<Item> items = itemRepository.findByOwnerId(ownerId, sortedById);
        final LocalDateTime nowDateTime = LocalDateTime.now();
        return items.stream()
                .map(item -> makeItem(item, nowDateTime, true))
                .sorted(Comparator.comparing(ItemResponseDto::getId))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ItemResponseDto getItemById(Long userId, Long itemId) {

        final Item itemById = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещи с id = " + itemId + " не существует"));
        boolean isOwner = userId.equals(itemById.getOwner().getId());
        return makeItem(itemById, LocalDateTime.now(), isOwner);
    }

    @Override
    @Transactional
    public ItemResponseDto createItem(Long ownerId, ItemCreateDto newItemDto) {

        final UserResponseDto owner = userService.getUserById(ownerId);
        try {
            final Item item = itemMapper.toItem(newItemDto, userMapper.toUser(owner));
            final Item createdItem = itemRepository.save(item);
            return itemMapper.toItemDto(createdItem);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException(ex.getMessage());
        }
    }

    @Override
    @Transactional
    public ItemResponseDto updateItem(Long ownerId, Long itemId, ItemResponseDto itemData) {

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
    public List<ItemResponseDto> searchItemsByText(String text, Integer from, Integer size) {

        if (text == null || text.isEmpty() || text.isBlank() || size == 0) {
            return new ArrayList<>();
        }
        Pageable sortedById = PageRequest.of(from / size, size, Sort.by("id").ascending());
        return itemRepository.searchItemsByText(text, sortedById)
                .stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentResponseDto createComment(Long authorId, Long itemId, CommentRequestDto newComment) {

        final LocalDateTime now = LocalDateTime.now();
        if (!bookingRepository
                .existsByItemIdAndBookerIdAndEndIsBeforeAndStatusId(
                        itemId,
                        authorId,
                        now,
                        StatusBooking.APPROVED)) {
            throw new ValidationException("Пользователь с id: " + authorId + " не пользовался вещью с id: " + itemId
                    + " или у него не закончился срок аренды");
        }
        final UserResponseDto authorDto = userService.getUserById(authorId);
        final ItemResponseDto itemResponseDto = getItemById(authorId, itemId);
        final User author = userMapper.toUser(authorDto);
        final Comment comment = commentMapper.toComment(newComment,
                author,
                itemMapper.toItem(itemResponseDto, author),
                now);
        final Comment createdComment = commentRepository.save(comment);
        return commentMapper.toCommentDto(createdComment, itemMapper.toItemDto(comment.getItem()));
    }

    private ItemResponseDto makeItem(Item item, LocalDateTime nowDateTime, boolean isOwner) {

        final Long itemId = item.getId();
        final Booking bookingLast = (isOwner)
                ? bookingRepository
                .findFirstByItemIdAndStatusIdAndStartBeforeOrderByEndDesc(itemId, StatusBooking.APPROVED, nowDateTime)
                : null;
        final BookingDto bookingDtoLast = Optional.ofNullable(bookingLast)
                .map(last -> bookingMapper.toBookingDto(last, itemMapper.toItemDto(last.getItem()),
                        userMapper.toUserDto(last.getBooker())))
                .orElse(null);
        List<StatusBooking> statuses = List.of(StatusBooking.WAITING, StatusBooking.APPROVED);
        final Booking bookingNext = (isOwner)
                ? bookingRepository
                .findFirstByItemIdAndStatusIdInAndStartAfterOrderByStartAsc(itemId, statuses, nowDateTime)
                : null;
        final BookingDto bookingDtoNext = Optional.ofNullable(bookingNext)
                .map(next -> bookingMapper.toBookingDto(next, itemMapper.toItemDto(next.getItem()),
                        userMapper.toUserDto(next.getBooker())))
                .orElse(null);
        List<CommentResponseDto> comments = commentRepository.findAllByItemId(itemId)
                .stream()
                .map(comment -> commentMapper.toCommentDto(comment, itemMapper.toItemDto(comment.getItem())))
                .collect(Collectors.toList());
        return itemMapper.toItemDto(item,
                userMapper.toUserDto(item.getOwner()),
                bookingMapper.toBookingBriefDto(bookingDtoLast),
                bookingMapper.toBookingBriefDto(bookingDtoNext), comments);
    }

}

package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.data.StatusBooking;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public List<ItemDto> getOwnerItems(Long ownerId) {
        List<Item> items = itemRepository.findByOwnerId(ownerId);
        List<ItemDto> itemsDto = new ArrayList<>();
        final LocalDateTime nowDateTime = LocalDateTime.now();
        for (Item item : items) {
            Long itemId = item.getId();
            final Booking bookingLast = bookingRepository
                    .findFirstByItemIdAndStatusIdAndEndBeforeOrderByEndDesc(itemId,
                            StatusBooking.APPROVED, nowDateTime);
            final BookingDto bookingDtoLast = bookingMapper.toBookingDto(bookingLast);
            List<StatusBooking> statuses = List.of(StatusBooking.WAITING, StatusBooking.APPROVED);
            final Booking bookingNext = bookingRepository
                    .findFirstByItemIdAndStatusIdInAndStartAfterOrderByStartAsc(itemId,
                            statuses, nowDateTime);
            final BookingDto bookingDtoNext = bookingMapper.toBookingDto(bookingNext);
            List<Comment> comments = commentRepository.findAllByItemId(itemId);
            ItemDto itemDto = itemMapper.toItemDto(item,
                    bookingMapper.toBookingBriefDto(bookingDtoLast),
                    bookingMapper.toBookingBriefDto(bookingDtoNext), comments);
            itemsDto.add(itemDto);
        }
        return itemsDto.stream().sorted(Comparator.comparing(ItemDto::getId)).collect(Collectors.toList());

    }

    @Override
    @Transactional
    public ItemDto getItemById(Long userId, Long itemId) {

        final Item itemById = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещи с id = " + itemId + " не существует"));
        boolean isOwner = userId.equals(itemById.getOwner().getId());
        final LocalDateTime nowDateTime = LocalDateTime.now();
        final Booking bookingLast = (isOwner)
                ? bookingRepository
                .findFirstByItemIdAndStatusIdAndEndBeforeOrderByEndDesc(itemId, StatusBooking.APPROVED, nowDateTime)
                : null;
        final BookingDto bookingDtoLast = bookingMapper.toBookingDto(bookingLast);
        List<StatusBooking> statuses = List.of(StatusBooking.WAITING, StatusBooking.APPROVED);
        final Booking bookingNext = (isOwner) //StatusIdIn(Collection<Age> ages)
                ? bookingRepository
                .findFirstByItemIdAndStatusIdInAndStartAfterOrderByStartAsc(itemId, statuses, nowDateTime)
                : null;
        final BookingDto bookingDtoNext = bookingMapper.toBookingDto(bookingNext);

        List<Comment> comments = commentRepository.findAllByItemId(itemId);
        return itemMapper.toItemDto(itemById,
                bookingMapper.toBookingBriefDto(bookingDtoLast),
                bookingMapper.toBookingBriefDto(bookingDtoNext), comments);
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

    @Override
    @Transactional
    public CommentDto createComment(Long authorId, Long itemId, CommentDto newComment) {
        final UserDto author = userService.getUserById(authorId);
        final ItemDto item = getItemById(authorId, itemId);
        if (bookingRepository
                //.existsBookingByBookerIdAndItemIdAndStartBeforeAndStatusId(authorId, itemId,
                //LocalDateTime.now(), StatusBooking.APPROVED)
                .existsByItem_IdAndBooker_IdAndEndIsBeforeAndStatusId(itemId,
                authorId, LocalDateTime.now(), StatusBooking.APPROVED)

        ) {
            throw new ValidationException("Пользователь с id: " + authorId + "не пользовался вещью с id: " + itemId);
        }
        final Comment comment = commentRepository.save(commentMapper.toComment(newComment, author, item));
        return commentMapper.toCommentDto(comment);
    }
}

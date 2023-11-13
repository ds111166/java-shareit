package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.data.StatusBooking;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserService userService;
    private final ItemService itemService;
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;

    @Override
    @Transactional
    public BookingDto createBooking(Long bookerId, BookingRequestDto newBookingRequestDto) {
        UserResponseDto booker = userService.getUserById(bookerId);
        final Long itemId = newBookingRequestDto.getItemId();
        final ItemResponseDto item = itemService.getItemById(bookerId, itemId);
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь с id: " + itemId + " не доступна для бронирования");
        }
        final Long ownerItemId = item.getOwner().getId();
        if (bookerId.equals(ownerItemId)) {
            throw new NotFoundException("Владелец id: " + ownerItemId
                    + " бронируется пользователем с id: " + bookerId +
                    "Бронирование владельцем своей вещи не допустимо");
        }
        try {
            Booking booking = bookingMapper.toBooking(newBookingRequestDto,
                    itemMapper.toItem(item, userMapper.toUser(item.getOwner())),
                    userMapper.toUser(booker),
                    StatusBooking.WAITING);
            Booking createdBooking = bookingRepository.save(booking);
            return bookingMapper.toBookingDto(createdBooking,
                    itemMapper.toItemDto(createdBooking.getItem()),
                    userMapper.toUserDto(createdBooking.getBooker()));
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException(ex.getMessage());
        }
    }

    @Override
    @Transactional
    public BookingDto approvalBooking(Long ownerItemId, Long bookingId, Boolean approved) {
        userService.getUserById(ownerItemId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирования с id = " + bookingId + " не существует"));
        ItemResponseDto item = itemService.getItemById(ownerItemId, booking.getItem().getId());
        if (!ownerItemId.equals(item.getOwner().getId())) {
            throw new NotFoundException("Пользователь с id: " + ownerItemId
                    + " не является владельцем вещи, которая бронируется с id: " + bookingId);
        }
        if (booking.getEnd().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Время бронирования уже истекло: " + booking.getEnd());
        }
        if (booking.getStatusId().equals(StatusBooking.CANCELED)) {
            throw new ValidationException("Бронирование отменено, статус: " + booking.getStatusId());
        }
        if (!booking.getStatusId().equals(StatusBooking.WAITING)) {
            throw new ValidationException("Решение по бронированию уже принято!");
        }
        booking.setStatusId((approved) ? StatusBooking.APPROVED : StatusBooking.REJECTED);
        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toBookingDto(savedBooking,
                itemMapper.toItemDto(savedBooking.getItem()),
                userMapper.toUserDto(savedBooking.getBooker()));
    }

    @Override
    @Transactional
    public BookingDto getBookingById(Long userId, Long bookingId) {
        userService.getUserById(userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирования с id = " + bookingId + " не существует"));
        User booker = booking.getBooker();
        boolean isBooker = userId.equals(booker.getId());
        ItemResponseDto item = itemService.getItemById(userId, booking.getItem().getId());
        boolean isOwnerItem = userId.equals(item.getOwner().getId());
        if (!isBooker && !isOwnerItem) {
            throw new NotFoundException("Пользователь с id: " + userId
                    + " не является ни владельцем вещи ни автором бронирования с id: " + bookingId);
        }
        return bookingMapper.toBookingDto(booking,
                itemMapper.toItemDto(booking.getItem()),
                userMapper.toUserDto(booking.getBooker()));
    }

    @Override
    @Transactional
    public List<BookingDto> getBookings(Long bookerId, String state) {
        userService.getUserById(bookerId);
        List<Booking> bookings;
        final Sort sorting = Sort.by(Sort.Order.desc("start"));
        final LocalDateTime nowDateTime = LocalDateTime.now();
        switch (state) {
            case "ALL":
                bookings = bookingRepository.findByBookerId(bookerId, sorting);
                break;
            case "CURRENT":
                bookings = bookingRepository.findByBookerIdAndStartLessThanAndEndGreaterThanEqual(bookerId,
                        nowDateTime, nowDateTime, sorting);
                break;
            case "PAST":
                bookings = bookingRepository.findByBookerIdAndEndBefore(bookerId, nowDateTime, sorting);
                break;
            case "FUTURE":
                bookings = bookingRepository.findByBookerIdAndStartAfter(bookerId, nowDateTime, sorting);
                break;
            case "WAITING":
                bookings = bookingRepository.findByBookerIdAndStatusId(bookerId,
                        StatusBooking.WAITING, sorting);
                break;
            case "REJECTED":
                bookings = bookingRepository.findByBookerIdAndStatusId(bookerId,
                        StatusBooking.REJECTED, sorting);
                break;
            default:
                throw new ValidationException("Unknown state: " + state);
        }
        return bookings.stream()
                .map(booking -> bookingMapper.toBookingDto(booking,
                        itemMapper.toItemDto(booking.getItem()),
                        userMapper.toUserDto(booking.getBooker())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<BookingDto> getBookingsByOwnerItemId(Long ownerItemId, String state) {
        userService.getUserById(ownerItemId);
        List<Booking> bookings;
        final Sort sorting = Sort.by(Sort.Order.desc("start"));
        final LocalDateTime nowDateTime = LocalDateTime.now();
        switch (state) {
            case "ALL":
                bookings = bookingRepository.findByItem_Owner_Id(ownerItemId, sorting);
                break;
            case "CURRENT":
                bookings = bookingRepository.findByItem_Owner_IdAndStartLessThanAndEndGreaterThanEqual(ownerItemId,
                        nowDateTime, nowDateTime, sorting);
                break;
            case "PAST":
                bookings = bookingRepository.findByItem_Owner_IdAndEndBefore(ownerItemId, nowDateTime, sorting);
                break;
            case "FUTURE":
                bookings = bookingRepository.findByItem_Owner_IdAndStartAfter(ownerItemId, nowDateTime, sorting);
                break;
            case "WAITING":
                bookings = bookingRepository.findByItem_Owner_IdAndStatusId(ownerItemId,
                        StatusBooking.WAITING, sorting);
                break;
            case "REJECTED":
                bookings = bookingRepository.findByItem_Owner_IdAndStatusId(ownerItemId,
                        StatusBooking.REJECTED, sorting);
                break;
            default:
                throw new ValidationException("Unknown state: " + state);
        }
        return bookings.stream()
                .map(booking -> bookingMapper.toBookingDto(booking,
                        itemMapper.toItemDto(booking.getItem()),
                        userMapper.toUserDto(booking.getBooker())))
                .collect(Collectors.toList());
    }
}

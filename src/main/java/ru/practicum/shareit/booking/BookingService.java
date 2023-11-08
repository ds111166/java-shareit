package ru.practicum.shareit.booking;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingData;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    BookingDto createBooking(Long bookerId, BookingData newBookingData);

    BookingDto approvalBooking(Long ownerItemId, Long bookingId, Boolean approved);

    BookingDto getBookingById(Long userId, Long bookingId);

    List<BookingDto> getBookings(Long bookerId, String state);

    List<BookingDto> getBookingsByOwnerItemId(Long ownerItemId, String state);

    BookingDto findFirstByItem_IdAndEndAfterOrderByStartDesc(Long itemId, LocalDateTime nowDateTime);

    BookingDto findFirstByItem_IdAndStartAfterOrderByEndAsc(Long itemId, LocalDateTime nowDateTime);
}
package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingData;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(Long bookerId, BookingData newBookingData);

    BookingDto approvalBooking(Long ownerItemId, Long bookingId, Boolean approved);

    BookingDto getBookingById(Long userId, Long bookingId);

    List<BookingDto> getBookings(Long bookerId, String state);

    List<BookingDto> getBookingsByOwnerItemId(Long ownerItemId, String state);
}

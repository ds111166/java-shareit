package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(Long bookerId, BookingRequestDto newBookingRequestDto);

    BookingDto approvalBooking(Long ownerItemId, Long bookingId, Boolean approved);

    BookingDto getBookingById(Long userId, Long bookingId);

    List<BookingDto> getBookings(Long bookerId, String state,
                                 Integer from, Integer size);

    List<BookingDto> getBookingsByOwnerItemId(Long ownerItemId, String state,
                                              Integer from, Integer size);
}

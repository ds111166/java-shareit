package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.data.State;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(BookingDto newBooking);

    BookingDto approvalBooking(Long ownerItemId, Long bookingId, Boolean approved);

    BookingDto getBookingById(Long userId, Long bookingId);

    List<BookingDto> getBookings(Long bookerId, State state);

    List<BookingDto> getBookingsByOwnerItemId(Long ownerItemId, State state);
}

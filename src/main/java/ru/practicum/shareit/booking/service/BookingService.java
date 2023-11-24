package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import javax.validation.constraints.Min;
import java.util.List;

public interface BookingService {
    BookingDto createBooking(Long bookerId, BookingRequestDto newBookingRequestDto);

    BookingDto approvalBooking(Long ownerItemId, Long bookingId, Boolean approved);

    BookingDto getBookingById(Long userId, Long bookingId);

    List<BookingDto> getBookings(Long bookerId, String state,
                                 @Min(0) Integer from, @Min(1) Integer size);

    List<BookingDto> getBookingsByOwnerItemId(Long ownerItemId, String state,
                                              @Min(0) Integer from, @Min(1) Integer size);
}

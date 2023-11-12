package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.data.StatusBooking;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;

@Component
@RequiredArgsConstructor
public class BookingMapper {

    public BookingDto toBookingDto(Booking booking,
                                   ItemResponseDto itemResponseDto,
                                   UserResponseDto bookerDto) {
        return (booking == null) ? null : BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(itemResponseDto)
                .booker(bookerDto)
                .status(booking.getStatusId())
                .build();
    }

    public BookingResponseDto toBookingBriefDto(BookingDto bookingDto) {
        return (bookingDto == null) ? null : BookingResponseDto.builder()
                .id(bookingDto.getId())
                .bookerId(bookingDto.getBooker().getId())
                .startTime(bookingDto.getStart())
                .endTime(bookingDto.getEnd())
                .build();
    }

    public Booking toBooking(BookingRequestDto newBookingRequestDto,
                             Item item,
                             User booker,
                             StatusBooking statusBooking) {
        return Booking.builder()
                .id(null)
                .start(newBookingRequestDto.getStart())
                .end(newBookingRequestDto.getEnd())
                .item(item)
                .booker(booker)
                .statusId(statusBooking)
                .build();
    }
}

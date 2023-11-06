package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.data.StatusBooking;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

@Component
@RequiredArgsConstructor
public class BookingMapper {
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;

    public BookingDto toBookingDto(Booking booking) {
        return (booking == null) ? null : BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(itemMapper.toItemDto(booking.getItem()))
                .booker(userMapper.toUserDto(booking.getBooker()))
                .statusId(booking.getStatusId())
                //.status(booking.getStatus())
                .build();

    }

    public Booking toBooking(BookingDto bookingDto) {
        return (bookingDto == null) ? null : Booking.builder()
                .id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(itemMapper.toItem(bookingDto.getItem()))
                .booker(userMapper.toUser(bookingDto.getBooker()))
                .statusId(bookingDto.getStatusId())
                //.status(booking.getStatus())
                .build();
    }

    public Booking toBooking(BookingDto bookingDto, StatusBooking statusId) {
        return (bookingDto == null) ? null : Booking.builder()
                .id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(itemMapper.toItem(bookingDto.getItem()))
                .booker(userMapper.toUser(bookingDto.getBooker()))
                .statusId(statusId)
                .build();
    }
}

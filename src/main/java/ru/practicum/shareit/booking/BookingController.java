package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingData;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.data.State;
import ru.practicum.shareit.validation.Marker;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @Validated({Marker.OnCreate.class})
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                    @Valid @RequestBody BookingData newBookingData) {
        log.info("Запрос на создание: \"{}\" от пользователя с id: {}", newBookingData, bookerId);
        final BookingDto booking = bookingService.createBooking(bookerId, newBookingData);
        log.info("Создан: \"{}\"", booking);
        return booking;
    }

    @PatchMapping("/{bookingId}")
    @Validated({Marker.OnUpdate.class})
    @ResponseStatus(HttpStatus.OK)
    public BookingDto approvalBooking(@RequestHeader("X-Sharer-User-Id") Long ownerItemId,
                                      @PathVariable @NotNull Long bookingId,
                                      @RequestParam(value = "approved", required = true) @NotNull Boolean approved) {
        log.info("Обработка запроса на бронирование с id: {}, владелец вещи id: {}, подверждение: {}",
                bookingId, ownerItemId, approved);
        final BookingDto booking = bookingService.approvalBooking(ownerItemId, bookingId, approved);
        log.info("Обработка запроса на бронирование с id: {}, владелец вещи id: {}, подверждение: {} - завершена",
                bookingId, ownerItemId, approved);
        return booking;
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable @NotNull Long bookingId) {
        log.info("Запрос на получение информации о бронировании с id: {} от пользователя с id: {}", bookingId, userId);
        final BookingDto booking = bookingService.getBookingById(userId, bookingId);
        log.info("Информация о бронировании с id: {} передана пользователю с id: {}", bookingId, userId);
        return booking;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getBookings(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                        @RequestParam(value = "state",
                                                defaultValue = "ALL",
                                                required = false) State state) {
        log.info("Запрос на получение списка бронирований в состоянии: \"{}\" пользователя с id: {}",
                state, bookerId);
        List<BookingDto> bookings = bookingService.getBookings(bookerId, state);
        log.info("Количество бронирований в состоянии: \"{}\" пользователя с id: {} равно: {}",
                state, bookerId, bookings.size());
        return bookings;
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getBookingsByOwnerItemId(@RequestHeader("X-Sharer-User-Id") Long ownerItemId,
                                                     @RequestParam(value = "state",
                                                             defaultValue = "ALL",
                                                             required = false) State state) {
        log.info("Запрос на получение списка бронирований в состоянии: \"{}\" вещей владельца с id: {}",
                state, ownerItemId);
        List<BookingDto> bookings = bookingService.getBookingsByOwnerItemId(ownerItemId, state);
        log.info("Количество бронирований в состоянии: \"{}\" вещей владельца с id: {} равно: {}",
                state, ownerItemId, bookings.size());
        return bookings;
    }
}

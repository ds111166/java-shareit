package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto createBooking(
            @RequestHeader("X-Sharer-User-Id") Long bookerId,
            @RequestBody BookingRequestDto newBookingRequestDto) {

        log.info("Запрос на создание: \"{}\" от пользователя с id: {}", newBookingRequestDto, bookerId);
        final BookingDto booking = bookingService.createBooking(bookerId, newBookingRequestDto);
        log.info("Создан: \"{}\"", booking);
        return booking;
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto approvalBooking(
            @RequestHeader("X-Sharer-User-Id") Long ownerItemId,
            @PathVariable Long bookingId,
            @RequestParam(value = "approved") Boolean approved) {

        log.info("Обработка запроса на бронирование с id: {}, владелец вещи id: {}, подтверждение: {}",
                bookingId, ownerItemId, approved);
        final BookingDto booking = bookingService.approvalBooking(ownerItemId, bookingId, approved);
        log.info("Обработка запроса на бронирование с id: {}, владелец вещи id: {}, подтверждение: {} - завершена",
                bookingId, ownerItemId, approved);
        return booking;
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto getBookingById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId) {

        log.info("Запрос на получение информации о бронировании с id: {} от пользователя с id: {}", bookingId, userId);
        final BookingDto booking = bookingService.getBookingById(userId, bookingId);
        log.info("Информация о бронировании с id: {} передана пользователю с id: {}", bookingId, userId);
        return booking;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getBookings(
            @RequestHeader("X-Sharer-User-Id") Long bookerId,
            @RequestParam(value = "state", defaultValue = "ALL", required = false) String state,
            @RequestParam(value = "from", defaultValue = "0") Integer from,
            @RequestParam(value = "size", required = false) Integer size) {

        log.info("Запрос на получение списка бронирований в состоянии: \"{}\" пользователя с id: {}\n" +
                        "Индекс первого элемента: {}. Количество элементов для отображения: {}",
                state, bookerId, from, size);
        List<BookingDto> bookings = bookingService.getBookings(bookerId, state, from,
                (size == null) ? Integer.MAX_VALUE : size);
        log.info("Количество бронирований в состоянии: \"{}\" пользователя с id: {} равно: {}",
                state, bookerId, bookings.size());
        log.info("Идентификаторы бронирований: \"{}\"",
                bookings.stream().map(BookingDto::getId).collect(Collectors.toList()));
        return bookings;
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getBookingsByOwnerItemId(
            @RequestHeader("X-Sharer-User-Id") Long ownerItemId,
            @RequestParam(value = "state", defaultValue = "ALL", required = false) String state,
            @RequestParam(value = "from", defaultValue = "0") Integer from,
            @RequestParam(value = "size", required = false) Integer size) {

        log.info("Запрос на получение списка бронирований в состоянии: \"{}\" вещей владельца с id: {}\n" +
                        "Индекс первого элемента: {}. Количество элементов для отображения: {}",
                state, ownerItemId, from, size);
        List<BookingDto> bookings = bookingService.getBookingsByOwnerItemId(ownerItemId, state, from,
                (size == null) ? Integer.MAX_VALUE : size);
        log.info("Количество бронирований в состоянии: \"{}\" вещей владельца с id: {} равно: {}",
                state, ownerItemId, bookings.size());
        log.info("Идентификаторы бронирований: \"{}\"",
                bookings.stream().map(BookingDto::getId).collect(Collectors.toList()));
        return bookings;
    }
}

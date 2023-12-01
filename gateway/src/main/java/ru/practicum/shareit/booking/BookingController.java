package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.validation.Marker;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {


    private final BookingClient bookingClient;

    @PostMapping
    @Validated({Marker.OnCreate.class})
    public ResponseEntity<Object> createBooking(
            @RequestHeader("X-Sharer-User-Id") Long bookerId,
            @Valid @RequestBody BookingRequestDto newBookingRequestDto) {
        log.info("Creating booking {}, userId={}", newBookingRequestDto, bookerId);
        return bookingClient.createBooking(bookerId, newBookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    @Validated({Marker.OnUpdate.class})
    public ResponseEntity<Object> approvalBooking(
            @RequestHeader("X-Sharer-User-Id") Long ownerItemId,
            @PathVariable @NotNull Long bookingId,
            @RequestParam(value = "approved") @NotNull Boolean approved) {
        log.info("Approval booking id={}, ownerItemId={}, approved={}", bookingId, ownerItemId, approved);
        return bookingClient.approvalBooking(ownerItemId, bookingId, approved);
    }


    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable @NotNull Long bookingId) {
        log.info("Get booking by id={}, userId={}", bookingId, userId);
        return bookingClient.getBookingById(userId, bookingId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getBookings(
            @RequestHeader("X-Sharer-User-Id") Long bookerId,
            @RequestParam(value = "state", defaultValue = "ALL", required = false) String stateParam,
            @Min(value = 0, message = "Индекс первого элемента не должен быть меньше нуля!")
            @RequestParam(value = "from", defaultValue = "0") Integer from,
            @Min(value = 1, message = "Количество элементов для отображения не должно быть меньше единицы!")
            @RequestParam(value = "size", required = false) Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new ValidationException("Unknown state: " + stateParam));
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, bookerId, from, size);
        return bookingClient.getBookings(bookerId, state, from, (size == null) ? Integer.MAX_VALUE : size);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getBookingsByOwnerItemId(
            @RequestHeader("X-Sharer-User-Id") Long ownerItemId,
            @RequestParam(value = "state", defaultValue = "ALL", required = false) String stateParam,
            @Min(value = 0, message = "Индекс первого элемента не должен быть меньше нуля!")
            @RequestParam(value = "from", defaultValue = "0") Integer from,
            @Min(value = 1, message = "Количество элементов для отображения не должно быть меньше единицы!")
            @RequestParam(value = "size", required = false) Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new ValidationException("Unknown state: " + stateParam));
        log.info("Get booking by owner id={} with state {}, from={}, size={}", ownerItemId, state, from, size);
        return bookingClient.getBookingsByOwnerItemId(ownerItemId,
                state, from, (size == null) ? Integer.MAX_VALUE : size);
    }
}

package ru.practicum.shareit.booking.data;

import lombok.Getter;

@Getter
public enum StatusBooking {
    WAITING,
    APPROVED,
    REJECTED,
    CANCELED
}

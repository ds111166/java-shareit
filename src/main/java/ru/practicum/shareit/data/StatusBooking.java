package ru.practicum.shareit.data;

import lombok.Getter;

@Getter
public enum StatusBooking {
    WAITING,
    APPROVED,
    REJECTED,
    CANCELED;
    /*
    WAITING(1),
    APPROVED(2),
    REJECTED(3),
    CANCELED(4);
    private final int id;

    StatusBooking(int id) {
        this.id = id;
    }

    public static StatusBooking getStatus(int id) {
        switch (id) {
            case 1:
                return WAITING;
            case 2:
                return APPROVED;
            case 3:
                return REJECTED;
            case 4:
                return CANCELED;
        }
        return null;
    }
*/
}

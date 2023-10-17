package ru.practicum.shareit.booking;

public enum StatusBooking {
    APPROVED(1),
    CANCELED(2),
    REJECTED(3),
    WAITING(4);
    private final int id;

    StatusBooking(int id) {
        this.id = id;
    }

    public static StatusBooking getStatus(int id) {
        switch (id) {
            case 1:
                return APPROVED;
            case 2:
                return CANCELED;
            case 3:
                return REJECTED;
            case 4:
                return WAITING;
        }
        return null;
    }

    public int getId() {
        return id;
    }
}

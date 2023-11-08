package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingBriefDto {
    private Long id;
    private Long bookerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}

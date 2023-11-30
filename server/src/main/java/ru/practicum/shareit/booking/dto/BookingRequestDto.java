package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validation.DataBookingValidate;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@DataBookingValidate
@AllArgsConstructor
public class BookingRequestDto {
    private Long itemId;
    @FutureOrPresent(message = "должно содержать сегодняшнее число или дату, которая еще не наступила")
    @NotNull(message = "не должно равняться null")
    private LocalDateTime start;
    @Future(message = "должно содержать дату, которая еще не наступила")
    @NotNull(message = "не должно равняться null")
    private LocalDateTime end;

}

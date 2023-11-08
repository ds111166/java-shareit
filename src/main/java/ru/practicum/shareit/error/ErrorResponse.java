package ru.practicum.shareit.error;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private final String timestamp;
    private final String status;
    private final String error;
    //private final String message;
}
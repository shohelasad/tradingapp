package com.trading.app.exception;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@AllArgsConstructor
@Builder
public class ErrorMessages {
    private int statusCode;
    private LocalDateTime timestamp;
    private String message;
    private String description;
    private Map<String, String> errors;
}

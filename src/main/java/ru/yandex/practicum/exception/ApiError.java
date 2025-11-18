package ru.yandex.practicum.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
public class ApiError {
    private String status;
    private String reason;
    private String message;
    private String timestamp;
}

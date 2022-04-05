package com.example.tictactoe.exceptions;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ApiErrorResponse {

    private String message;
    private String errorCode;
    private LocalDateTime timestamp;
    private List<String> fieldErrors;
}

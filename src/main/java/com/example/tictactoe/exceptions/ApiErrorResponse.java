package com.example.tictactoe.exceptions;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class ApiErrorResponse {

    private String message;
    private String errorCode;
    private LocalDateTime timestamp;
    private List<String> fieldErrors;
}

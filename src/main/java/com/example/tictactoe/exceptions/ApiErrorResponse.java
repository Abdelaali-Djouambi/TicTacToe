package com.example.tictactoe.exceptions;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class HttpErrorResponse {

    HttpStatus status;
    private String message;
    private Integer errorCode;
    private LocalDateTime timestamp;
    private List<String> subErrors;
}

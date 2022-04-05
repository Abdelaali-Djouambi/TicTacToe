package com.example.tictactoe.exceptions;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequestException(BadRequestException badRequestException){
        ApiErrorResponse errorResponse = new ApiErrorResponse();
        errorResponse.setErrorCode(ErrorCode.BAD_REQUEST_EXCEPTION.name());
        errorResponse.setMessage(badRequestException.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<> (errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(ResourceNotFoundException resourceNotFoundException){
        ApiErrorResponse errorResponse = new ApiErrorResponse();
        errorResponse.setErrorCode(ErrorCode.RESOURCE_NOT_FOUND.name());
        errorResponse.setMessage(resourceNotFoundException.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<> (errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotAllowedException.class)
    public ResponseEntity<ApiErrorResponse> handleNotAllowedException(NotAllowedException notAuthorizedException){
        ApiErrorResponse errorResponse = new ApiErrorResponse();
        errorResponse.setErrorCode(ErrorCode.NOT_ALLOWED_EXCEPTION.name());
        errorResponse.setMessage(notAuthorizedException.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<> (errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse();
        List<String> errors =ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        errorResponse.setErrorCode(ErrorCode.NOT_ALLOWED_EXCEPTION.name());
        errorResponse.setMessage("Field validation errors");
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setFieldErrors(errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}

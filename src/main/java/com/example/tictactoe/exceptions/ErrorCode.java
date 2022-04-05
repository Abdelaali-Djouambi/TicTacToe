package com.example.tictactoe.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    RESOURCE_NOT_FOUND("Expected resource not found"),
    NOT_ALLOWED_EXCEPTION("Action is not allowed"),
    BAD_REQUEST_EXCEPTION("Cannot perform action"),
    VALIDATION_EXCEPTION("Values submitted are wrong"),
    PLAYER_NOT_FOUND("Player not found : "),
    PLAYER_ALREADY_IN_GAME("Player already in game");
    private final String defaultMessage;
}

package com.example.tictactoe.service;

import com.example.tictactoe.dto.GameDTO;
import com.example.tictactoe.dto.PlayDTO;

import java.util.Optional;

public interface GameService {
    Optional<GameDTO> initGame(String playerAlias);
    Optional<GameDTO> gameStatus(Long gameId);
    Optional<GameDTO> cancelGame(Long gameId);
    Optional<GameDTO> play(PlayDTO playDTO);
    Optional<GameDTO> findById(Long gameId);

}

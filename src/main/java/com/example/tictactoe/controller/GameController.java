package com.example.tictactoe.controller;

import com.example.tictactoe.DTO.GameDTO;
import com.example.tictactoe.DTO.StartGameDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class GameControler {

    @PostMapping(path="/initGame", produces = "application/json", consumes = "application/json")
    public ResponseEntity<GameDTO> initGame(@RequestBody StartGameDTO startGameDTO){
        return null;
    }
}

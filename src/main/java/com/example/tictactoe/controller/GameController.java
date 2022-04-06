package com.example.tictactoe.controller;

import com.example.tictactoe.dto.GameDTO;
import com.example.tictactoe.dto.PlayDTO;
import com.example.tictactoe.service.GameService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/game")
public class GameController {

    public static final String LOCATION = "Location";
    public static final String GAME = "/game/";
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }


    @ApiResponses(value={
            @ApiResponse(code = 201, response = GameDTO.class,message = "Player 1 creates a new game"),
            @ApiResponse(code = 200, response = GameDTO.class,message = "Player 2 joins an existing game"),
            @ApiResponse(code = 404, response = GameDTO.class,message = "Player not found"),
            @ApiResponse(code = 400, response = GameDTO.class,message = "Player already in game")
    })
    @Operation(description = "When we call this endpoint the first time, Player X (player with the sent alias foo or faa) creates a game and waits for " +
            "the second player, Player O, to join the game. </br>" +
            "Once a second player calls this endpoint, he join.")
    @PostMapping(path = "/startGame", produces = "application/json", consumes = "application/json")
    public ResponseEntity<GameDTO> startGame(@RequestBody @Valid String playerAlias) {
        return gameService.initGame(playerAlias).map(gameDTO ->
        {
            if (gameDTO.getPlayerO() != null) {
                return ResponseEntity
                        .ok()
                        .header(LOCATION, GAME + gameDTO.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .eTag(Long.toString(gameDTO.getVersion()))
                        .body(gameDTO);
            } else {
                return ResponseEntity
                        .created(URI.create(GAME + gameDTO.getId().toString()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .eTag(Long.toString(gameDTO.getVersion()))
                        .body(gameDTO);
            }
        }).orElse(ResponseEntity.badRequest().build());
    }

    @ApiResponses(value={
            @ApiResponse(code = 200, response = GameDTO.class,message = "Player played successfully"),
            @ApiResponse(code = 404, response = GameDTO.class,message = "Player not found or game not found"),
            @ApiResponse(code = 400, response = GameDTO.class,message = "Not the player's turn, game board position is already filled"),
            @ApiResponse(code = 405, response = GameDTO.class,message = "Game ended, or player not in game")
    })
    @Operation(description = "Player plays in one of the game-board's frames, the game-board has 9 frames.")
    @PostMapping(path = "/play", produces = "application/json", consumes = "application/json")
    public ResponseEntity<GameDTO> play(@Valid @RequestBody PlayDTO playDTO) {
        return gameService.play(playDTO).map(gameDTO ->
                ResponseEntity
                        .ok()
                        .header(LOCATION, GAME + gameDTO.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .eTag(Long.toString(gameDTO.getVersion()))
                        .body(gameDTO)).orElse(ResponseEntity.notFound().build());
    }

    @ApiResponses(value={
            @ApiResponse(code = 200, response = GameDTO.class,message = "Game found, details sent."),
            @ApiResponse(code = 404, response = GameDTO.class,message = "Game not found"),
    })
    @Operation(description = "Returns a game details.")
    @GetMapping(path = "/{gameId}/status", produces = "application/json", consumes = "application/json")
    public ResponseEntity<GameDTO> gameStatus(@PathVariable Long gameId) {
        return gameService.gameStatus(gameId).map(
                gameDTO -> ResponseEntity
                        .ok()
                        .header(LOCATION, GAME + gameId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .eTag(Long.toString(gameDTO.getVersion()))
                        .body(gameDTO)).orElse(ResponseEntity.notFound().build());
    }

    @ApiResponses(value={
            @ApiResponse(code = 200, response = GameDTO.class,message = "Game canceled"),
            @ApiResponse(code = 404, response = GameDTO.class,message = "Game not found"),
            @ApiResponse(code = 405, response = GameDTO.class,message = "Game can't be cancelled, game status must not be final")
    })
    @Operation(description = "Cancels a game.")
    @PostMapping(path = "/cancel", produces = "application/json", consumes = "application/json")
    public ResponseEntity<GameDTO> cancelGame(@RequestBody @Valid Long gameId) {
        return gameService.cancelGame(gameId).map(
                gameDTO -> ResponseEntity
                        .ok()
                        .header(LOCATION, GAME + "cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .eTag(Long.toString(gameDTO.getVersion()))
                        .body(gameDTO)).orElse(ResponseEntity.notFound().build());
    }
}

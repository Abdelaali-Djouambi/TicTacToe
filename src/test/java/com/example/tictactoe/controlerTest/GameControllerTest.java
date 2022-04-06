package com.example.tictactoe.controlerTest;

import com.example.tictactoe.controller.GameController;
import com.example.tictactoe.dto.GameDTO;
import com.example.tictactoe.dto.PlayDTO;
import com.example.tictactoe.fixture.GameFixture;
import com.example.tictactoe.model.Game;
import com.example.tictactoe.service.GameService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

    @InjectMocks
    GameController gameController;

    @Mock
    GameService gameService;

    @Test
    @DisplayName("POST /game - Start game first player")
    void initGameFirstPlayerX() {
        GameDTO gameDTO = GameFixture.getGameDTO(false);
        when(gameService.initGame(any(String.class))).thenReturn(Optional.of(gameDTO));

        ResponseEntity<GameDTO> responseEntity = gameController.startGame("foo");

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(responseEntity.getHeaders().getLocation().getPath()).isEqualTo("/game/1");
        assertThat(responseEntity.getBody().getVersion()).isEqualTo(Long.valueOf(1));
        assertThat(responseEntity.getBody().getStatus()).isEqualTo(Game.Status.WAITING_OPPONENT.name());
        assertThat(responseEntity.getBody().getPlayerX().getFirstName()).isEqualTo(gameDTO.getPlayerX().getFirstName());
    }


    @Test
    @DisplayName("POST /game - Start game second player")
    void initGameSecondPlayerO() {
        GameDTO gameDTO = GameFixture.getGameDTO(true);
        when(gameService.initGame(any(String.class))).thenReturn(Optional.of(gameDTO));

        ResponseEntity<GameDTO> responseEntity = gameController.startGame("faa");

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(responseEntity.getHeaders().getLocation().getPath()).isEqualTo("/game/1");
        assertThat(responseEntity.getBody().getVersion()).isEqualTo((Long.valueOf(2)));
        assertThat(responseEntity.getBody().getStatus()).isEqualTo((Game.Status.X_TURN.name()));
        assertThat(responseEntity.getBody().getPlayerX().getFirstName()).isEqualTo((gameDTO.getPlayerX().getFirstName()));
        assertThat(responseEntity.getBody().getPlayerX().getLastName()).isEqualTo((gameDTO.getPlayerX().getLastName()));

}

    @Test
    @DisplayName("GET /game - Returns game details")
    void gameStatusSuccess() {
        PlayDTO playDTO = new PlayDTO("foo",0,1l);
        GameDTO gameDTO = GameFixture.getGameDTO(true);
        gameDTO.setBoard(GameFixture.getGameMapDraw());
        gameDTO.getBoard().put(0, Game.FRAME_VALUE.X.toString());
        gameDTO.setStatus(Game.Status.X_TURN.toString());
        when(gameService.gameStatus(any(Long.class))).thenReturn(Optional.of(gameDTO));
        Long gameId=12l;
        ResponseEntity<GameDTO> responseEntity = gameController.gameStatus(gameId);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(responseEntity.getHeaders().getLocation().getPath()).isEqualTo("/game/"+gameId+"/status");
        assertThat(responseEntity.getBody().getVersion()).isEqualTo(Long.valueOf(2));
        assertThat(responseEntity.getBody().getStatus()).isEqualTo(Game.Status.X_TURN.toString());
        assertThat(responseEntity.getBody().getBoard()).containsEntry(0,Game.FRAME_VALUE.X.name());
        assertThat(responseEntity.getBody().getPlayerX().getFirstName()).isEqualTo(gameDTO.getPlayerX().getFirstName());
    }
    @Test
    @DisplayName("GET /game - Game id not found")
    void gameStatusErrorNotFound() {

        when(gameService.gameStatus(any(Long.class))).thenReturn(Optional.empty());
        Long gameId=12l;

        ResponseEntity<GameDTO> responseEntity = gameController.gameStatus(gameId);

        Assertions.assertEquals(HttpStatus.NOT_FOUND,responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("GET /game - Cancel an ongoing game success")
    void cancelGameSuccess() {
        PlayDTO playDTO = new PlayDTO("foo",0,1l);
        GameDTO gameDTO = GameFixture.getGameDTO(true);
        gameDTO.setBoard(GameFixture.getGameMapDraw());
        gameDTO.setStatus(Game.Status.CANCELED.toString());
        when(gameService.cancelGame(any(Long.class))).thenReturn(Optional.of(gameDTO));
        Long gameId=12l;
        ResponseEntity<GameDTO> responseEntity = gameController.cancelGame(gameId);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(responseEntity.getHeaders().getLocation().getPath()).isEqualTo("/game/cancel");
        assertThat(responseEntity.getBody().getVersion()).isEqualTo((Long.valueOf(2)));
        assertThat(responseEntity.getBody().getStatus()).isEqualTo((Game.Status.CANCELED.toString()));
        assertThat(responseEntity.getBody().getPlayerX().getFirstName()).isEqualTo((gameDTO.getPlayerX().getFirstName()));
    }
    @Test
    @DisplayName("GET /game - Game id not found")
    void cancelGameErrorNotFound() {

        when(gameService.cancelGame(any(Long.class))).thenReturn(Optional.empty());

        ResponseEntity<GameDTO> responseEntity = gameController.cancelGame(12l);

        Assertions.assertEquals(HttpStatus.NOT_FOUND,responseEntity.getStatusCode());
    }
}

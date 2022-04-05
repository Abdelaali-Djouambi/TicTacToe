package com.example.tictactoe.serviceTest;

import com.example.tictactoe.dto.GameDTO;
import com.example.tictactoe.dto.PlayDTO;
import com.example.tictactoe.exceptions.BadRequestException;
import com.example.tictactoe.exceptions.NotAllowedException;
import com.example.tictactoe.exceptions.ResourceNotFoundException;
import com.example.tictactoe.fixture.GameFixture;
import com.example.tictactoe.model.Game;
import com.example.tictactoe.repository.GameRepository;
import com.example.tictactoe.repository.PlayerRepository;
import com.example.tictactoe.service.GameServiceImpl;
import com.example.tictactoe.util.ModelMapperUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static com.example.tictactoe.model.Game.FRAME_VALUE;
import static com.example.tictactoe.model.Game.Status;
import static com.example.tictactoe.model.Game.Status.CANCELED;
import static com.example.tictactoe.model.Game.Status.DRAW;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @InjectMocks
    GameServiceImpl gameService;

    @Mock
    PlayerRepository playerRepository;

    @Mock
    GameRepository gameRepository;

    @Mock
    ModelMapperUtil modelMapperUtil;

    @Test
    @DisplayName("Init game - success first player")
    void initGameFirstPlayer() {

        Game game = GameFixture.getGame(false);
        GameDTO gameDTO = GameFixture.getGameDTO(false);

        when(playerRepository.findByAlias(any(String.class))).thenReturn(Optional.of(game.getPlayerX()));
        when(gameRepository.findByStatusInAndPlayerX_IdOrStatusInAndPlayerO_Id(any(List.class), nullable(Long.class), any(List.class), nullable(Long.class))).thenReturn((Collections.emptyList()));
        when(gameRepository.findByStatus(any(Status.class))).thenReturn((Collections.emptyList()));
        when(gameRepository.save(any(Game.class))).thenReturn(game);
        when(modelMapperUtil.mapGameToGameDTO(any(Game.class))).thenReturn(gameDTO);

        Optional<GameDTO> optionalGameDTO = gameService.initGame("foo");

        Assertions.assertEquals("foo", optionalGameDTO.get().getPlayerX().getFirstName());
        Assertions.assertEquals(1l, optionalGameDTO.get().getVersion());
        Assertions.assertEquals(Status.WAITING_OPPONENT.toString(), optionalGameDTO.get().getStatus());

    }


    @Test
    @DisplayName("Init game - success second player")
    void initGameSecondPlayer() {

        Game game = GameFixture.getGame(true);

        GameDTO gameDTO = GameFixture.getGameDTO(true);

        List<Game> gameList = Arrays.asList(new Game[]{game});

        when(playerRepository.findByAlias(any(String.class))).thenReturn(Optional.of(game.getPlayerO()));
        when(modelMapperUtil.mapGameToGameDTO(any(Game.class))).thenReturn(gameDTO);
        when(gameRepository.findByStatusInAndPlayerX_IdOrStatusInAndPlayerO_Id(any(List.class), any(Long.class), any(List.class), any(Long.class))).thenReturn((Collections.emptyList()));
        when(gameRepository.findByStatus(any(Status.class))).thenReturn((gameList));
        when(gameRepository.save(any(Game.class))).thenReturn(game);

        Optional<GameDTO> optionalGameDTO = gameService.initGame("faa");

        Assertions.assertEquals("foo", optionalGameDTO.get().getPlayerX().getFirstName());
        Assertions.assertEquals("faa", optionalGameDTO.get().getPlayerO().getFirstName());
        Assertions.assertEquals(2l,optionalGameDTO.get().getVersion());
        Assertions.assertEquals(optionalGameDTO.get().getStatus(), Status.X_TURN.toString());
    }

    @Test
    @DisplayName("Init game - Error Bad request player not found")
    void initGameErrorPlayerNotFound() {

        Game mockGame = GameFixture.getGame(false);
        List<Game> gameList = Arrays.asList(new Game[]{mockGame});

        when(playerRepository.findByAlias(any(String.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> gameService.initGame("foo"));
    }

    @Test
    @DisplayName("Init game - Error Bad request player already in game")
    void initGameErrorPlayerAlreadyInGame() {

        Game game = GameFixture.getGame(false);
        List<Game> gameList = Arrays.asList(new Game[]{game});

        when(playerRepository.findByAlias(any(String.class))).thenReturn(Optional.of(game.getPlayerX()));
        when(gameRepository.findByStatusInAndPlayerX_IdOrStatusInAndPlayerO_Id(any(List.class), nullable(Long.class), any(List.class), nullable(Long.class))).thenReturn(gameList);

        assertThrows(BadRequestException.class, () -> gameService.initGame("foo"));
    }


    @Test
    @DisplayName("Play game - player X puts first value in a frame of the game board - success")
    void playGameSuccess() {

        PlayDTO play = new PlayDTO("foo", 0, 10l);
        Game game = GameFixture.getGame(true);
        game.setVersion(3l);
        game.setStatus(Status.X_TURN);

        GameDTO gameDTO = GameFixture.getGameDTO(true);
        gameDTO.setVersion(3l);
        gameDTO.setStatus(Status.O_TURN.toString());
        gameDTO.getBoard().put(0, FRAME_VALUE.X.toString());
        when(playerRepository.findByAlias(any(String.class))).thenReturn(Optional.of(game.getPlayerX()));
        when(gameRepository.findById(nullable(Long.class))).thenReturn(Optional.of(game));
        when(modelMapperUtil.mapGameToGameDTO(any(Game.class))).thenReturn(gameDTO);
        when(gameRepository.save(any(Game.class))).thenReturn(game);

        Optional<GameDTO> optionalGameDTO = gameService.play(play);

        Assertions.assertEquals("foo", optionalGameDTO.get().getPlayerX().getFirstName());
        Assertions.assertEquals("faa", optionalGameDTO.get().getPlayerO().getFirstName());
        Assertions.assertEquals(3l,optionalGameDTO.get().getVersion());
        Assertions.assertEquals(optionalGameDTO.get().getStatus(), Status.O_TURN.toString());
        Assertions.assertEquals(optionalGameDTO.get().getBoard().get(0), FRAME_VALUE.X.toString());


    }

    @Test
    @DisplayName("Play game - player X plays and wins the game")
    void playGameXWins() {

        PlayDTO play = new PlayDTO("foo", 2, 10l);
        Map<Integer, String> gameBoardDTO = GameFixture.getGameMapForXToWin();
        GameDTO gameDTO = GameFixture.getGameDTO(true);
        gameDTO.setVersion(3l);
        gameDTO.setStatus(Status.WINNER_X.toString());
        gameDTO.setBoard(gameBoardDTO);

        Map<Integer, FRAME_VALUE> gameBoard = new HashMap<>();
        gameBoardDTO.entrySet().stream().forEach(e -> gameBoard.put(e.getKey(), FRAME_VALUE.valueOf(e.getValue())));
        Game game = GameFixture.getGame(true);
        game.setVersion(3l);
        game.setStatus(Status.X_TURN);
        game.setBoard(gameBoard);

        when(playerRepository.findByAlias(any(String.class))).thenReturn(Optional.of(game.getPlayerX()));
        when(gameRepository.findById(nullable(Long.class))).thenReturn(Optional.of(game));
        gameDTO.getBoard().put(2, "X");
        when(modelMapperUtil.mapGameToGameDTO(any(Game.class))).thenReturn(gameDTO);
        when(gameRepository.save(any(Game.class))).thenReturn(game);

        Optional<GameDTO> optionalGameDTO = gameService.play(play);

        Assertions.assertEquals("foo", optionalGameDTO.get().getPlayerX().getFirstName());
        Assertions.assertEquals("faa", optionalGameDTO.get().getPlayerO().getFirstName());
        Assertions.assertEquals(3l,optionalGameDTO.get().getVersion());
        Assertions.assertEquals(Status.WINNER_X.toString(),optionalGameDTO.get().getStatus());
        Assertions.assertEquals( FRAME_VALUE.X.toString(),optionalGameDTO.get().getBoard().get(2));
    }

    @Test
    @DisplayName("Play game - player O plays and wins the game")
    void playGameOWins() {

        PlayDTO play = new PlayDTO("faa", 5, 10l);
        Map<Integer, String> gameBoardDTO = GameFixture.getGameMapForOToWin();
        GameDTO gameDTO = GameFixture.getGameDTO(true);
        gameDTO.setVersion(3l);
        gameDTO.setStatus(Status.WINNER_O.toString());
        gameDTO.setBoard(gameBoardDTO);

        Map<Integer, FRAME_VALUE> gameBoard = new HashMap<>();
        gameBoardDTO.entrySet().stream().forEach(e -> gameBoard.put(e.getKey(), FRAME_VALUE.valueOf(e.getValue())));
        Game game = GameFixture.getGame(true);
        game.setVersion(3l);
        game.setStatus(Status.O_TURN);
        game.setBoard(gameBoard);

        when(playerRepository.findByAlias(any(String.class))).thenReturn(Optional.of(game.getPlayerX()));
        when(gameRepository.findById(nullable(Long.class))).thenReturn(Optional.of(game));
        gameDTO.getBoard().put(5, "O");
        when(modelMapperUtil.mapGameToGameDTO(any(Game.class))).thenReturn(gameDTO);
        when(gameRepository.save(any(Game.class))).thenReturn(game);

        Optional<GameDTO> optionalGameDTO = gameService.play(play);

        Assertions.assertEquals("foo", optionalGameDTO.get().getPlayerX().getFirstName());
        Assertions.assertEquals("faa", optionalGameDTO.get().getPlayerO().getFirstName());
        Assertions.assertEquals(3l,optionalGameDTO.get().getVersion());
        Assertions.assertEquals(Status.WINNER_O.toString(),optionalGameDTO.get().getStatus());
        Assertions.assertEquals( FRAME_VALUE.O.toString(),optionalGameDTO.get().getBoard().get(5));
    }

    @Test
    @DisplayName("Play game - player O plays and wins the game")
    void playGameDraw() {

        PlayDTO play = new PlayDTO("foo", 6, 10l);
        Map<Integer, String> gameBoardDTO = GameFixture.getGameMapDraw();
        GameDTO gameDTO = GameFixture.getGameDTO(true);
        gameDTO.setVersion(3l);
        gameDTO.setStatus(DRAW.toString());
        gameDTO.setBoard(gameBoardDTO);

        Map<Integer, FRAME_VALUE> gameBoard = new HashMap<>();
        gameBoardDTO.entrySet().stream().forEach(e -> gameBoard.put(e.getKey(), FRAME_VALUE.valueOf(e.getValue())));
        Game game = GameFixture.getGame(true);
        game.setVersion(3l);
        game.setStatus(Status.X_TURN);
        game.setBoard(gameBoard);

        when(playerRepository.findByAlias(any(String.class))).thenReturn(Optional.of(game.getPlayerX()));
        when(gameRepository.findById(nullable(Long.class))).thenReturn(Optional.of(game));

        gameDTO.getBoard().put(6, "X");
        when(modelMapperUtil.mapGameToGameDTO(any(Game.class))).thenReturn(gameDTO);
        when(gameRepository.save(any(Game.class))).thenReturn(game);

        Optional<GameDTO> optionalGameDTO = gameService.play(play);

        Assertions.assertEquals("foo", optionalGameDTO.get().getPlayerX().getFirstName());
        Assertions.assertEquals("faa", optionalGameDTO.get().getPlayerO().getFirstName());
        Assertions.assertEquals(3l,optionalGameDTO.get().getVersion());
        Assertions.assertEquals(DRAW.toString(),optionalGameDTO.get().getStatus());
        Assertions.assertEquals(FRAME_VALUE.X.toString(),optionalGameDTO.get().getBoard().get(6));
    }

    @Test
    @DisplayName("Play game - throws exception  when player tries to play a game where he doesn't play - Bad request")
    void playGameExceptionPlayer() {

        PlayDTO play = new PlayDTO("fii", 6, 10l);
        Map<Integer, String> gameBoardDTO = GameFixture.getGameMapDraw();

        Map<Integer, FRAME_VALUE> gameBoard = new HashMap<>();
        gameBoardDTO.entrySet().stream().forEach(e -> gameBoard.put(e.getKey(), FRAME_VALUE.valueOf(e.getValue())));

        Game game = GameFixture.getGame(true);
        game.setVersion(3l);
        game.setStatus(Status.X_TURN);
        game.setBoard(gameBoard);

        when(playerRepository.findByAlias(any(String.class))).thenReturn(Optional.of(game.getPlayerX()));
        when(gameRepository.findById(nullable(Long.class))).thenReturn(Optional.of(game));

        Assertions.assertThrows(NotAllowedException.class, () -> gameService.play(play));
    }

    @Test
    @DisplayName("Find game - find game success")
    void findGameSuccess() {

        Game game = GameFixture.getGame(false);
        GameDTO gameDTO = GameFixture.getGameDTO(false);

        when(gameRepository.findById(nullable(Long.class))).thenReturn(Optional.of(game));
        when(modelMapperUtil.mapGameToGameDTO(any(Game.class))).thenReturn(gameDTO);

        Optional<GameDTO> optionalGameDTO = gameService.findById(1l);

        Assertions.assertEquals(1l,optionalGameDTO.get().getVersion());
        Assertions.assertEquals(optionalGameDTO.get().getStatus(), gameDTO.getStatus());
    }

    @Test
    @DisplayName("Cancel game - cancel success")
    void cancelGameSuccess() {

        Map<Integer, FRAME_VALUE> gameBoard = new HashMap<>();
        Game game = GameFixture.getGame(false);
        GameDTO gameDTO = GameFixture.getGameDTO(false);
        gameDTO.setStatus(CANCELED.toString());
        gameDTO.setVersion(2l);

        when(gameRepository.findById(nullable(Long.class))).thenReturn(Optional.of(game));
        when(gameRepository.save(nullable(Game.class))).thenReturn(game);
        when(modelMapperUtil.mapGameToGameDTO(any(Game.class))).thenReturn(gameDTO);
        Optional<GameDTO> optionalGameDTO = gameService.cancelGame(1l);
        Assertions.assertEquals(2l,optionalGameDTO.get().getVersion());
        Assertions.assertEquals(optionalGameDTO.get().getStatus(), gameDTO.getStatus());
    }

    @Test
    @DisplayName("Cancel game - game can't be canceled")
    void cancelGameError() {
        Map<Integer, FRAME_VALUE> gameBoard = new HashMap<>();
        Game game = GameFixture.getGame(false);
        game.setStatus(DRAW);
        when(gameRepository.findById(nullable(Long.class))).thenReturn(Optional.of(game));
        Assertions.assertThrows(BadRequestException.class, () -> gameService.cancelGame(2l));
    }

    @Test
    @DisplayName("Find game - game not found")
    void findGameErrorNotFound() {
        Map<Integer, FRAME_VALUE> gameBoard = new HashMap<>();
        Game game = GameFixture.getGame(false);
        GameDTO gameDTO = GameFixture.getGameDTO(false);
        when(gameRepository.findById(nullable(Long.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> gameService.findById(2l));
    }
}

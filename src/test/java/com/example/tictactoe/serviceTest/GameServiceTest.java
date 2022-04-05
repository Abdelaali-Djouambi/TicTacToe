package serviceTest;

import com.example.tictactoe.DTO.GameDTO;
import com.example.tictactoe.exceptions.BadRequestException;
import com.example.tictactoe.model.Game;
import com.example.tictactoe.model.Player;
import com.example.tictactoe.repository.GameRepository;
import com.example.tictactoe.repository.PlayerRepository;
import com.example.tictactoe.service.GameService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class GameServiceTest {

    GameService gameService;

    @MockBean
    PlayerRepository playerRepository;

    @MockBean
    GameRepository gameRepository;

    @Test
    @DisplayName("Init game - success first player")
    void initGameFirstPlayer() {

        Game mockGame = new Game();
        Player mockPlayer = new Player("foo", "foo", "foo");
        mockGame.setId(1l);
        mockGame.setVersion(1l);
        mockGame.setStatus(Game.Status.WAITING_OPPONENT);
        mockGame.setPlayerX(mockPlayer);

        when(playerRepository.findById(any(Long.class))).thenReturn(Optional.of(mockPlayer));
        when(gameRepository.findByPlayerX_IdOrPlayerO_IdAndStatusIn(any(Long.class), any(Long.class), any(List.class))).thenReturn((Collections.emptyList()));
        when(gameRepository.findByStatus(any(Game.Status.class))).thenReturn((Collections.emptyList()));
        when(gameRepository.save(any(Game.class))).thenReturn(mockGame);

        GameDTO returnedGame = gameService.initGame("foo");

        Assertions.assertEquals("foo", returnedGame.getPlayerX().getAlias());
        Assertions.assertEquals(returnedGame.getVersion(), 1l);
        Assertions.assertEquals(returnedGame.getStatus(), Game.Status.WAITING_OPPONENT);

    }


    @Test
    @DisplayName("Init game - success second player")
    void initGameSecondPlayer() {

        Game mockGame = new Game();
        Player mockPlayer1 = new Player("foo", "foo", "foo");
        Player mockPlayer2 = new Player("faa", "faa", "faa");
        mockPlayer1.setId(1l);
        mockPlayer2.setId(2l);
        mockGame.setId(1l);
        mockGame.setVersion(1l);
        mockGame.setStatus(Game.Status.X_TURN);
        mockGame.setPlayerX(mockPlayer1);
        mockGame.setPlayerX(mockPlayer2);

        when(playerRepository.findById(1l)).thenReturn(Optional.of(mockPlayer1));
        when(playerRepository.findById(2l)).thenReturn(Optional.of(mockPlayer2));
        when(gameRepository.findByPlayerX_IdOrPlayerO_IdAndStatusIn(any(Long.class), any(Long.class), any(List.class))).thenReturn((Collections.emptyList()));
        when(gameRepository.findByStatus(any(Game.Status.class))).thenReturn((Collections.emptyList()));
        when(gameRepository.save(any(Game.class))).thenReturn(mockGame);

        GameDTO returnedGame = gameService.initGame("foo");

        Assertions.assertEquals("foo", returnedGame.getPlayerX().getAlias());
        Assertions.assertEquals("faa", returnedGame.getPlayerX().getAlias());
        Assertions.assertEquals(returnedGame.getVersion(), 2l);
        Assertions.assertEquals(returnedGame.getStatus(), Game.Status.X_TURN);
    }

    @Test
    @DisplayName("Init game - Error Bad request player not found")
    void initGameErrorPlayerNotFound() {

        Game mockGame = new Game();
        Player mockPlayer = new Player("foo", "foo", "foo");
        mockGame.setId(1l);
        mockGame.setVersion(1l);
        mockGame.setStatus(Game.Status.WAITING_OPPONENT);
        mockGame.setPlayerX(mockPlayer);
        List<Game> gameList = Arrays.asList(new Game[]{mockGame});
        when(playerRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        //when(gameRepository.findByPlayerX_IdOrPlayerO_IdAndStatusIn(any(Long.class), any(Long.class), any(List.class))).thenReturn(gameList);
        //when(gameRepository.findByStatus(any(Game.Status.class))).thenReturn((Collections.emptyList()));
        //Mockito.when(gameRepository.save(any(Game.class))).thenReturn(mockGame);
        assertThrows(BadRequestException.class, () -> gameService.initGame("foo"));
    }

    @Test
    @DisplayName("Init game - Error Bad request player already in game")
    void initGameErrorPlayerAlreadyInGame() {

        Game mockGame = new Game();
        Player mockPlayer = new Player("foo", "foo", "foo");
        mockGame.setId(1l);
        mockGame.setVersion(1l);
        mockGame.setStatus(Game.Status.WAITING_OPPONENT);
        mockGame.setPlayerX(mockPlayer);
        List<Game> gameList = Arrays.asList(new Game[]{mockGame});
        when(playerRepository.findById(any(Long.class))).thenReturn(Optional.of(mockPlayer));
        when(gameRepository.findByPlayerX_IdOrPlayerO_IdAndStatusIn(any(Long.class), any(Long.class), any(List.class))).thenReturn(gameList);
        when(gameRepository.findByStatus(any(Game.Status.class))).thenReturn((Collections.emptyList()));
        //Mockito.when(gameRepository.save(any(Game.class))).thenReturn(mockGame);
        GameDTO returnedGame = gameService.initGame("foo");

        assertThrows(BadRequestException.class, () -> gameService.initGame("foo"));
    }
}

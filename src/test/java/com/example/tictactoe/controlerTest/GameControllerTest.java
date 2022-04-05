package controlerTest;

import com.example.tictactoe.DTO.GameDTO;
import com.example.tictactoe.DTO.PlayerDTO;
import com.example.tictactoe.controller.GameController;
import com.example.tictactoe.model.Game;
import com.example.tictactoe.service.GameService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class GameControllerTest {

    @InjectMocks
    GameController gameController;

    @Mock
    GameService gameService;

    @Test
    @DisplayName("POST /game - Start game first player")
    public void testInitGamePlayerX() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        GameDTO mockGameDTO = new GameDTO();
        PlayerDTO mockPlayerX = new PlayerDTO("foo", "foo", "foo");
        mockGameDTO.setPlayerX(mockPlayerX);
        mockGameDTO.setStatus(Game.Status.WAITING_OPPONENT.toString());
        Mockito.when(gameService.initGame(any(String.class))).thenReturn(mockGameDTO);

        ResponseEntity<GameDTO> responseEntity = gameController.startGame("foo");

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);

        assertThat(responseEntity.getHeaders().getLocation().getPath()).isEqualTo("/1");

        assertThat(responseEntity.getBody().getVersion().equals(Long.valueOf(1)));
        assertThat(responseEntity.getBody().getStatus().equals(Game.Status.WAITING_OPPONENT));
        assertThat(responseEntity.getBody().getPlayerX().getAlias().equals(mockPlayerX.getAlias()));
    }

   /* @Test
    @DisplayName("POST /game - Join game secound player")
    public void testInitGamePlayerO() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        GameDTO mockGameDTO = new GameDTO();
        PlayerDTO mockPlayerDTO_X = new PlayerDTO("player_X", "X", "foo");
        PlayerDTO mockPlayerDTO_O = new PlayerDTO("player_O", "O", "fii");

        mockGameDTO.setPlayerX(mockPlayerDTO_X);
        mockGameDTO.setStatus(Game.Status.WAITING_OPPONENT.toString());
        Mockito.when(gameService.initGame(ArgumentMatchers.any(StartGameDTO.class))).thenReturn(mockGameDTO);

        StartGameDTO startGameDTO = new StartGameDTO("fii", 'O');
        ResponseEntity<GameDTO> responseEntity = gameController.startGame(startGameDTO);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);

        assertThat(responseEntity.getHeaders().getLocation().getPath()).isEqualTo("/1");

        assertThat(responseEntity.getBody().getVersion().equals(Long.valueOf(1)));
        assertThat(responseEntity.getBody().getStatus().equals(Game.Status.X_TURN));
        assertThat(responseEntity.getBody().getPlayerX().getAlias().equals(mockPlayerDTO_X.getAlias()));
        assertThat(responseEntity.getBody().getPlayerO().getAlias().equals(mockPlayerDTO_O.getAlias()));
    }*/

   /* @Test
    @DisplayName("POST /game - Bad request : player already in game")
    public void testInitGameBadRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        GameDTO mockGameDTO = new GameDTO();
        PlayerDTO mockPlayerX = new PlayerDTO("player_X", "X", "foo");
        PlayerDTO mockPlayerO = new PlayerDTO("player_O", "O", "fii");

        mockGameDTO.setPlayerX(mockPlayerX);
        mockGameDTO.setPlayerO(mockPlayerO);
        mockGameDTO.setStatus(Game.Status.X_TURN.toString());
        Mockito.when(gameService.initGame(ArgumentMatchers.any(StartGameDTO.class))).thenReturn(()-> throw new BadRequestException(""));

        StartGameDTO startGameDTO = new StartGameDTO("fii", 'O');
        ResponseEntity<GameDTO> responseEntity = gameController.startGame(startGameDTO);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(responseEntity.getHeaders().getLocation().getPath()).isEqualTo("/1");
    }
*/
}

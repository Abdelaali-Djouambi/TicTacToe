package com.example.tictactoe.fixture;

import com.example.tictactoe.dto.GameDTO;
import com.example.tictactoe.dto.PlayerDTO;
import com.example.tictactoe.model.Game;
import com.example.tictactoe.model.Player;

import java.util.HashMap;
import java.util.Map;

public class GameFixture {
    public static Game
    getGame(boolean twoPlayers) {
        Player playerX = new Player("foo", "foo", "foo");
        playerX.setId(1l);

        Game game = new Game();
        game.setId(1l);
        game.setPlayerX(playerX);

        if (twoPlayers) {
            Player playerO = new Player("faa", "faa", "faa");
            playerO.setId(2l);
            game.setPlayerO(playerO);
            game.setStatus(Game.Status.X_TURN);
            game.setVersion(2l);
        } else {
            game.setVersion(1l);
            game.setStatus(Game.Status.WAITING_OPPONENT);
        }
        return game;
    }

    public static GameDTO getGameDTO(boolean twoPlayers) {
        GameDTO gameDTO = new GameDTO();
        PlayerDTO playerDTO1 = new PlayerDTO("foo", "foo");
        playerDTO1.setId(1l);
        gameDTO.setId(1l);
        gameDTO.setPlayerX(playerDTO1);
        if (twoPlayers) {
            PlayerDTO playerDTO2 = new PlayerDTO("faa", "faa");
            playerDTO2.setId(2l);
            gameDTO.setPlayerO(playerDTO2);
            gameDTO.setStatus(Game.Status.X_TURN.toString());
            gameDTO.setVersion(2l);
        } else {
            gameDTO.setVersion(1l);
            gameDTO.setStatus(Game.Status.WAITING_OPPONENT.toString());
        }
        return gameDTO;
    }

    /*      --------------------
            |  X   |  X  |(2-x)|
            --------------------
            |  O   |  O  |     |
             -------------------
            |     |     |     |
            --------------------      */
    public static Map<Integer, String> getGameMapForXToWin() {
        Map<Integer, String> gameMap = new HashMap<>();
        gameMap.put(0, Game.FRAME_VALUE.X.toString());
        gameMap.put(1, Game.FRAME_VALUE.X.toString());
        gameMap.put(2, Game.FRAME_VALUE.EMPTY.toString());
        gameMap.put(3, Game.FRAME_VALUE.O.toString());
        gameMap.put(4, Game.FRAME_VALUE.EMPTY.toString());
        gameMap.put(5, Game.FRAME_VALUE.O.toString());
        gameMap.put(6, Game.FRAME_VALUE.EMPTY.toString());
        gameMap.put(7, Game.FRAME_VALUE.EMPTY.toString());
        gameMap.put(8, Game.FRAME_VALUE.EMPTY.toString());

        return gameMap;
    }

    /*      --------------------
            |  X   |  X  |     |
            --------------------
            |  O   |  O  |(5-o)|
            --------------------
            |     |   X  |     |
            --------------------      */
    public static Map<Integer, String> getGameMapForOToWin() {

        Map<Integer, String> gameMap = new HashMap<>();
        gameMap.put(0, Game.FRAME_VALUE.X.toString());
        gameMap.put(1, Game.FRAME_VALUE.X.toString());
        gameMap.put(2, Game.FRAME_VALUE.EMPTY.toString());
        gameMap.put(3, Game.FRAME_VALUE.O.toString());
        gameMap.put(4, Game.FRAME_VALUE.O.toString());
        gameMap.put(5, Game.FRAME_VALUE.EMPTY.toString());
        gameMap.put(6, Game.FRAME_VALUE.EMPTY.toString());
        gameMap.put(7, Game.FRAME_VALUE.X.toString());
        gameMap.put(8, Game.FRAME_VALUE.EMPTY.toString());
        return gameMap;
    }

    /*      --------------------
            |  O   |  X  |  O  |
            --------------------
            |  O   |  X  |  X  |
            --------------------
            |(6-x ) | O  |  X  |
            --------------------      */
    public static Map<Integer, String> getGameMapDraw() {
        Map<Integer, String> gameMap = new HashMap<>();
        gameMap.put(0, Game.FRAME_VALUE.O.toString());
        gameMap.put(1, Game.FRAME_VALUE.X.toString());
        gameMap.put(2, Game.FRAME_VALUE.O.toString());
        gameMap.put(3, Game.FRAME_VALUE.O.toString());
        gameMap.put(4, Game.FRAME_VALUE.X.toString());
        gameMap.put(5, Game.FRAME_VALUE.X.toString());
        gameMap.put(6, Game.FRAME_VALUE.EMPTY.toString());
        gameMap.put(7, Game.FRAME_VALUE.O.toString());
        gameMap.put(8, Game.FRAME_VALUE.X.toString());
        return gameMap;
    }
}

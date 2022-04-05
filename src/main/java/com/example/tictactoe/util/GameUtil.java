package com.example.tictactoe.util;

import com.example.tictactoe.model.Game;
import com.example.tictactoe.model.Game.FRAME_VALUE;
import com.example.tictactoe.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.stream.Collectors;

public class GameUtil {

    public static final String LINE_SEPARATION = "-------------------------";
    public static final String FRAME_PATTERN = "|   {}   |   {}   |   {}   |";
    static Logger log = LoggerFactory.getLogger(GameUtil.class);

    private GameUtil() {
    }

    public static Game startGame(Player player, Game game) {
        if (game == null) {
            game = new Game();
            game.setPlayerX(player);
            game.setStatus(Game.Status.WAITING_OPPONENT);
        } else {
            if (game.getPlayerX() == null) {
                game.setPlayerX(player);
            } else {
                game.setPlayerO(player);
            }
            game.setStatus(Game.Status.X_TURN);
        }
        return game;
    }

    public static void printGameMap(Map<Integer, FRAME_VALUE> gameMap) {
        if (log.isInfoEnabled()) {
            log.info(LINE_SEPARATION);
            log.info(FRAME_PATTERN, printFrameValue(gameMap.get(0)), printFrameValue(gameMap.get(1)), printFrameValue(gameMap.get(2)));
            log.info(LINE_SEPARATION);
            log.info(FRAME_PATTERN, printFrameValue(gameMap.get(3)), printFrameValue(gameMap.get(4)), printFrameValue(gameMap.get(5)));
            log.info(LINE_SEPARATION);
            log.info(FRAME_PATTERN, printFrameValue(gameMap.get(6)), printFrameValue(gameMap.get(7)), printFrameValue(gameMap.get(8)));
            log.info(LINE_SEPARATION);
        }
    }

    public static String printFrameValue(FRAME_VALUE frameValue) {
        return frameValue == FRAME_VALUE.EMPTY ? "-" : frameValue.toString();
    }

    public static Game.Status checkBoardState(Map<Integer, FRAME_VALUE> gameBoard) {
        for (int a = 0; a < 8; a++) {
            String line = "";
            switch (a) {
                case 0:
                    line = gameBoard.get(0).toString() + gameBoard.get(1).toString() + gameBoard.get(2).toString();
                    break;
                case 1:
                    line = gameBoard.get(3).toString() + gameBoard.get(4).toString() + gameBoard.get(5).toString();
                    break;
                case 2:
                    line = gameBoard.get(6).toString() + gameBoard.get(7).toString() + gameBoard.get(8).toString();
                    break;
                case 3:
                    line = gameBoard.get(0).toString() + gameBoard.get(3).toString() + gameBoard.get(6).toString();
                    break;
                case 4:
                    line = gameBoard.get(1).toString() + gameBoard.get(4).toString() + gameBoard.get(7).toString();
                    break;
                case 5:
                    line = gameBoard.get(2).toString() + gameBoard.get(5).toString() + gameBoard.get(8).toString();
                    break;
                case 6:
                    line = gameBoard.get(0).toString() + gameBoard.get(4).toString() + gameBoard.get(8).toString();
                    break;
                case 7:
                    line = gameBoard.get(2).toString() + gameBoard.get(4).toString() + gameBoard.get(6).toString();
                    break;
                default:
                    break;
            }
            if (line.equals("XXX")) {
                return Game.Status.WINNER_X;
            } else if (line.equals("OOO")) {
                return Game.Status.WINNER_O;
            }
        }
        printGameMap(gameBoard);
        if (gameBoard.values().stream().filter(value -> value != FRAME_VALUE.EMPTY).collect(Collectors.toList()).size() == 9) {
            return Game.Status.DRAW;
        }
        return null;
    }

}

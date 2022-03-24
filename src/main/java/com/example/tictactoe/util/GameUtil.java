package com.example.tictactoe.util;

import com.example.tictactoe.model.GameBoard;
import org.springframework.stereotype.Component;

@Component
public class GameUtil {

    public GameBoard.FRAME_VALUE getWinner(GameBoard gameBoard){
        if (gameBoard.getTopLeft() != GameBoard.FRAME_VALUE.NULL && gameBoard.getTopLeft() == gameBoard.getTop() && gameBoard.getTopLeft() == gameBoard.getTopRight()){
            return gameBoard.getTopLeft();
        }
        if (gameBoard.getTopLeft() != GameBoard.FRAME_VALUE.NULL && gameBoard.getTopLeft() == gameBoard.getCenterLeft() && gameBoard.getTopLeft() == gameBoard.getBottomLeft()){
            return gameBoard.getTopLeft();
        }
        if (gameBoard.getTopLeft() != GameBoard.FRAME_VALUE.NULL && gameBoard.getTopLeft() == gameBoard.getCenter() && gameBoard.getTopLeft() == gameBoard.getBottomRight()){
            return gameBoard.getTopLeft();
        }
        if (gameBoard.getTopRight() != GameBoard.FRAME_VALUE.NULL && gameBoard.getTopRight() == gameBoard.getCenter() && gameBoard.getTopRight() == gameBoard.getBottomLeft()){
            return gameBoard.getTopRight();
        }
        if (gameBoard.getBottomRight() != GameBoard.FRAME_VALUE.NULL && gameBoard.getBottomRight() == gameBoard.getCenterRight() && gameBoard.getBottomRight() == gameBoard.getTopRight()){
            return gameBoard.getBottomRight();
        }
        if (gameBoard.getBottomRight() != GameBoard.FRAME_VALUE.NULL && gameBoard.getBottomRight() == gameBoard.getBottom() && gameBoard.getBottomRight() == gameBoard.getBottomLeft()){
            return gameBoard.getBottomRight();
        }
        return GameBoard.FRAME_VALUE.NULL;
    }
}

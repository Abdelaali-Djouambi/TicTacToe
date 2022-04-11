package com.example.tictactoe.dto;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class GameDTO extends AbstractDTO{

    private Map<Integer,String> board;
    private PlayerDTO playerX;
    private PlayerDTO playerO;
    private String status;

    public GameDTO(){
        this.board = new HashMap<>();
        this.board.put(0, null);
        this.board.put(1, null);
        this.board.put(2, null);
        this.board.put(3, null);
        this.board.put(4, null);
        this.board.put(5, null);
        this.board.put(6, null);
        this.board.put(7, null);
        this.board.put(8, null);
    }

}

package com.example.tictactoe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "game_board")
public class GameBoard extends PersistedEntity{

    public enum FRAME_VALUE{
        X,O,NULL
    }
//test
    private FRAME_VALUE topLeft;
    private FRAME_VALUE top;
    private FRAME_VALUE topRight;
    private FRAME_VALUE centerLeft;
    private FRAME_VALUE center;
    private FRAME_VALUE centerRight;
    private FRAME_VALUE bottomLeft;
    private FRAME_VALUE bottom;
    private FRAME_VALUE bottomRight;




}

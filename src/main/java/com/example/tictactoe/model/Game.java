package com.example.tictactoe.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@Entity
@Table(name = "game")
public class Game extends AbstractEntity {

    public enum Status{
        WAITING_OPPONENT,
        X_TURN,
        O_TURN,
        CANCELED,
        DRAW,
        WINNER_X,
        WINNER_O
    }
    public enum FRAME_VALUE{
        X,O,EMPTY
    }

    @ElementCollection
    @MapKeyColumn(name="frame_position")
    @Column(name="frame_value", nullable = false)
    @CollectionTable(name="board_values", joinColumns=@JoinColumn(name="game_id"))
    Map<Integer, FRAME_VALUE> board = new HashMap<>();

    public Game(){
        this.board = new HashMap<>();
        this.board.put(0, FRAME_VALUE.EMPTY);
        this.board.put(1, FRAME_VALUE.EMPTY);
        this.board.put(2, FRAME_VALUE.EMPTY);
        this.board.put(3, FRAME_VALUE.EMPTY);
        this.board.put(4, FRAME_VALUE.EMPTY);
        this.board.put(5, FRAME_VALUE.EMPTY);
        this.board.put(6, FRAME_VALUE.EMPTY);
        this.board.put(7, FRAME_VALUE.EMPTY);
        this.board.put(8, FRAME_VALUE.EMPTY);
    }


    @OneToOne
    private Player playerX;

    @OneToOne
    private Player playerO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;


}

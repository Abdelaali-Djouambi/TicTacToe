package com.example.tictactoe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "game")
public class Game extends PersistedEntity {

    public enum Status{
        WAITING_OPPONENT,
        X_TURN,
        O_TURN,
        CANCELED,
        DRAW,
        WINNER
    }

    @OneToOne
    private GameBoard gameBoard;

    @OneToOne
    private Player X_Player;

    @OneToOne
    private Player O_Player;




}

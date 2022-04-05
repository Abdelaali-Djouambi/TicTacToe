package com.example.tictactoe;

import com.example.tictactoe.model.Player;
import com.example.tictactoe.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

public class TicTacToeRunner implements ApplicationRunner {
    @Autowired
    private PlayerRepository playerRepository;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        playerRepository.save(new Player("foo","foo","foo"));
        playerRepository.save(new Player("faa","faa","faa"));
        System.out.println("aaaaaaaaa");
    }
}

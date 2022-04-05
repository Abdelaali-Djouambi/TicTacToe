package com.example.tictactoe.config;

import com.example.tictactoe.model.Player;
import com.example.tictactoe.repository.PlayerRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class TicTacToeRunner  implements ApplicationListener<ApplicationReadyEvent> {
    private final PlayerRepository playerRepository;

    public TicTacToeRunner(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        playerRepository.save(new Player("foo","foo","foo"));
        playerRepository.save(new Player("faa","faa","faa"));
    }
}

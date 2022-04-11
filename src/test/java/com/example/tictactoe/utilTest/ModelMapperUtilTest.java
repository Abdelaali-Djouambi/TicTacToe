package com.example.tictactoe.utilTest;

import com.example.tictactoe.dto.GameDTO;
import com.example.tictactoe.fixture.GameFixture;
import com.example.tictactoe.model.Game;
import com.example.tictactoe.util.ModelMapperUtil;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

@ExtendWith(MockitoExtension.class)
class ModelMapperUtilTest {

    ModelMapperUtil modelMapper = new ModelMapperUtil(new ModelMapper());

    @Test
    void map_game_to_gameDTO(){
        Game game = GameFixture.getGame(true);
        GameDTO gameDTO = modelMapper.mapGameToGameDTO(game);
        Assertions.assertEquals(game.getStatus().toString(),gameDTO.getStatus());
        Assertions.assertEquals(game.getPlayerO().getFirstName(),gameDTO.getPlayerO().getFirstName());
        Assertions.assertEquals(game.getPlayerX().getFirstName(),gameDTO.getPlayerX().getFirstName());
    }
}

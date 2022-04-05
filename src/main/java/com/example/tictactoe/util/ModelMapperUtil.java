package com.example.tictactoe.util;

import com.example.tictactoe.dto.GameDTO;
import com.example.tictactoe.model.Game;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperUtil {

    final
    ModelMapper modelMapper;

    @Autowired
    public ModelMapperUtil(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public GameDTO mapGameToGameDTO(Game game) {
        return modelMapper.map(game, GameDTO.class);
    }
}

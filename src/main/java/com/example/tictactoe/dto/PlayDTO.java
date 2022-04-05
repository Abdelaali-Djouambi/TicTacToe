package com.example.tictactoe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayDTO {

    @NotBlank
    private String playerAlias;
    @NotBlank
    private int position;
    @NotBlank
    private Long id;

}

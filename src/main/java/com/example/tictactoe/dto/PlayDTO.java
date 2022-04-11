package com.example.tictactoe.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayDTO {

    @NotBlank
    private String playerAlias;
    @Max(8)
    @Min(0)
    @NotNull
    private int position;
    @NotNull
    private Long gameId;

}

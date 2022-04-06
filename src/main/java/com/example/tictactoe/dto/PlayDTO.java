package com.example.tictactoe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
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
    private Long id;

}

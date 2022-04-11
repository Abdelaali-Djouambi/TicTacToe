package com.example.tictactoe.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDTO extends AbstractDTO{

    private String firstName;
    private String lastName;
}

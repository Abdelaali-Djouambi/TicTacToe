package com.example.tictactoe.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public abstract class AbstractDTO {

    private Long id;
    private Date createDate;
    private Date lastUpdateTime;
    private Long version;
}

package com.example.tictactoe.dto;

import lombok.Data;

import java.util.Date;

@Data
public abstract class AbstractDTO {

    private Long id;
    private Date createDate;
    private Date lastUpdateTime;
    private Long version;
}

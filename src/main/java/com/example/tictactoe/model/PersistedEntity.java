package com.example.tictactoe.model;


import lombok.Data;

import javax.persistence.*;


@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class PersistedEntity {
    @Id
    private Long id;

    private Long version;


    @PreUpdate
    private void incrementVersion(){
        this.version = version++;
    }
    @PrePersist
    private void initVersion(){
        this.version = Long.valueOf("1");
    }
}

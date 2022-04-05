package com.example.tictactoe.model;


import jdk.jfr.Timestamp;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;


@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long version;

    @Timestamp
    @Column(name="create_date")
    private Date createDate;

    @Timestamp
    @Column(name="last_update_date")
    private Date lastUpdateDate;


    @PreUpdate
    private void updateInfo(){
        this.version++;
        this.lastUpdateDate= new Date();
    }
    @PrePersist
    private void initVersion(){
        this.createDate = new Date();
        this.version = Long.valueOf("1");
    }
}

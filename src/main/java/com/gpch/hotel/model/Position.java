package com.gpch.hotel.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "position")
public class Position {
    @Id
    @Column(name = "position_id")
    private int id;
    @Column(name = "position")
    private String position_name;

}

package com.gpch.hotel.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name = "maintenance")
public class Maintenance {
    @Id
    @Column(name = "maintenance_id")
    private long id;
    @Column(name = "first_name")
    private String firstname;
    @Column(name = "last_name")
    private String lastname;
    @Column(name = "room")
    private String room;
    @Column(name = "status")
    private String status;
    @Column(name = "description")
    private String description;
    @Column(name = "date")
    private Date date;
}

package com.gpch.hotel.model;


import com.fasterxml.jackson.annotation.JsonBackReference;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {
    @Id
    @Column(name = "product_id")
    private long id;
    @Column(name = "productname")
    private String productName;
    @Column(name = "price")
    private int price;
    @Column(name = "amount")
    private int amount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name="store_id", nullable=false)
    private Store stores;


    public long getIdStore() {
        return stores.getId();
    }

}

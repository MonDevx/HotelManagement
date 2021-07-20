package com.gpch.hotel.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "store")
public class Store {

    @Column(name = "store_id")
    private long id;

    @Column(name = "storename")
    private String storeName;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "stores", cascade = CascadeType.REMOVE,fetch = FetchType.EAGER)
    private List<Product> products;

    public String Listnameproducts() {
        ArrayList<String> Productnames = new ArrayList<>();
        for (Product temp : products) Productnames.add(temp.getProductname());
        return Productnames.toString();
    }
}

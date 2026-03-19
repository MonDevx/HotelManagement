package com.gpch.hotel.model

import javax.persistence.*

@Entity
@Table(name = "store")
class Store {

    @Id
    @Column(name = "store_id")
    var id: Long = 0

    @Column(name = "storename")
    var storeName: String? = null

    @Column(name = "status")
    var status: String? = null

    @OneToMany(mappedBy = "stores", cascade = [CascadeType.REMOVE], fetch = FetchType.EAGER)
    var products: MutableList<Product> = mutableListOf()

    fun listnameproducts(): String {
        val productNames = ArrayList<String>()
        for (temp in products) productNames.add(temp.productName ?: "")
        return productNames.toString()
    }
}

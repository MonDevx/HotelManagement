package com.gpch.hotel.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
@Table(name = "product")
class Product {

    @Id
    @Column(name = "product_id")
    var id: Long = 0

    @Column(name = "productname")
    var productName: String? = null

    @Column(name = "price")
    var price: Int = 0

    @Column(name = "amount")
    var amount: Int = 0

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "store_id", nullable = false)
    var stores: Store? = null

    fun getIdStore(): Long = stores?.id ?: 0L
}

package com.gpch.hotel.model

import javax.persistence.*

@Entity
@Table(name = "room_type")
class RoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_type_id")
    var id: Long = 0

    @Column(name = "name")
    var name: String? = null

    @Column(name = "description")
    var description: String? = null

    @Column(name = "capacity")
    var capacity: Int = 1

    @Column(name = "price")
    var price: Int = 0

    @Column(name = "amenities")
    var amenities: String? = null
}

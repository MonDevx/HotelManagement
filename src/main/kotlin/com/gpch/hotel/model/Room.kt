package com.gpch.hotel.model

import javax.persistence.*

@Entity
@Table(name = "room")
class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    var id: Long = 0

    @Column(name = "room_number", unique = true)
    var roomNumber: String? = null

    @Column(name = "status")
    var status: String = "available"

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_type_id", nullable = false)
    var roomType: RoomType? = null
}

package com.gpch.hotel.model

import javax.persistence.*

@Entity
@Table(name = "position")
class Position {

    @Id
    @Column(name = "position_id")
    var id: Int = 0

    @Column(name = "position")
    var position_name: String? = null
}

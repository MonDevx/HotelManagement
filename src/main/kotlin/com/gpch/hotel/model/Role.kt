package com.gpch.hotel.model

import javax.persistence.*

@Entity
@Table(name = "role")
class Role {

    @Id
    @Column(name = "role_id")
    var id: Int = 0

    @Column(name = "role")
    var role: String? = null
}

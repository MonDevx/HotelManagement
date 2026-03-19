package com.gpch.hotel.model

import java.sql.Date
import javax.persistence.*

@Entity
@Table(name = "maintenance")
class Maintenance {

    @Id
    @Column(name = "maintenance_id")
    var id: Long = 0

    @Column(name = "first_name")
    var firstname: String? = null

    @Column(name = "last_name")
    var lastname: String? = null

    @Column(name = "room")
    var room: String? = null

    @Column(name = "status")
    var status: String? = null

    @Column(name = "description")
    var description: String? = null

    @Column(name = "date")
    var date: Date? = null
}

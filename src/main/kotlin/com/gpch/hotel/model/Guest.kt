package com.gpch.hotel.model

import javax.persistence.*

@Entity
@Table(name = "guest")
class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guest_id")
    var id: Long = 0

    @Column(name = "first_name")
    var firstName: String? = null

    @Column(name = "last_name")
    var lastName: String? = null

    @Column(name = "phone")
    var phone: String? = null

    @Column(name = "email")
    var email: String? = null

    @Column(name = "identity_number")
    var identityNumber: String? = null

    @OneToMany(mappedBy = "guest", fetch = FetchType.LAZY)
    var bookings: MutableList<Booking> = mutableListOf()

    fun fullName(): String = listOf(firstName, lastName).filterNot { it.isNullOrBlank() }.joinToString(" ")
}

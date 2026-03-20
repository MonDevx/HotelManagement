package com.gpch.hotel.model

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "payment")
class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    var id: Long = 0

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booking_id", nullable = false)
    var booking: Booking? = null

    @Column(name = "amount")
    var amount: Int = 0

    @Column(name = "payment_method")
    var paymentMethod: String? = null

    @Column(name = "payment_type")
    var paymentType: String? = null

    @Column(name = "payment_date")
    var paymentDate: LocalDate? = null

    @Column(name = "note")
    var note: String? = null
}

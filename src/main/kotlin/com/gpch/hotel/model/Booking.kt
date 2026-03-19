package com.gpch.hotel.model

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "booking")
class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    var id: Long = 0

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "guest_id", nullable = false)
    var guest: Guest? = null

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_type_id", nullable = false)
    var roomType: RoomType? = null

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    var room: Room? = null

    @Column(name = "check_in_date")
    var checkInDate: LocalDate? = null

    @Column(name = "check_out_date")
    var checkOutDate: LocalDate? = null

    @Column(name = "guest_count")
    var guestCount: Int = 1

    @Column(name = "discount_percent")
    var discountPercent: Int = 0

    @Column(name = "promotion_discount")
    var promotionDiscount: Int = 0

    @Column(name = "promotion_code")
    var promotionCode: String? = null

    @Column(name = "additional_charges")
    var additionalCharges: Int = 0

    @Column(name = "total_price")
    var totalPrice: Int = 0

    @Column(name = "status")
    var status: String = "pending"

    @Column(name = "walk_in")
    var walkIn: Boolean = false
}

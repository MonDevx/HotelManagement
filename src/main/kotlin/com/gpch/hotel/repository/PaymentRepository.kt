package com.gpch.hotel.repository

import com.gpch.hotel.model.Payment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository("paymentRepository")
interface PaymentRepository : JpaRepository<Payment, Long> {
    @Query("select coalesce(sum(p.amount), 0) from Payment p where p.booking.id = :bookingId")
    fun totalPaidForBooking(@Param("bookingId") bookingId: Long): Int

    @Query("select coalesce(sum(p.amount), 0) from Payment p where p.paymentDate between :start and :end")
    fun sumAmountBetween(@Param("start") start: LocalDate, @Param("end") end: LocalDate): Int
}

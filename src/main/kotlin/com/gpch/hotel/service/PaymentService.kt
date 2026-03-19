package com.gpch.hotel.service

import com.gpch.hotel.model.Payment
import com.gpch.hotel.repository.PaymentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service("paymentService")
@Transactional
class PaymentService(private val paymentRepository: PaymentRepository) {

    fun findAll(): List<Payment> = paymentRepository.findAll().sortedByDescending { it.paymentDate ?: LocalDate.MIN }

    fun findById(id: Long): Payment? = paymentRepository.findById(id).orElse(null)

    fun save(payment: Payment): Payment {
        require(payment.booking != null) { "Booking is required" }
        require(payment.amount > 0) { "Payment amount must be greater than zero" }
        if (payment.paymentDate == null) {
            payment.paymentDate = LocalDate.now()
        }
        return paymentRepository.save(payment)
    }

    fun deleteById(id: Long) = paymentRepository.deleteById(id)

    fun totalPaidForBooking(bookingId: Long): Int = paymentRepository.totalPaidForBooking(bookingId)
}

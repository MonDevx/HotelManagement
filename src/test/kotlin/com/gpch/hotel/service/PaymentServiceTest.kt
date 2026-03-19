package com.gpch.hotel.service

import com.gpch.hotel.model.Booking
import com.gpch.hotel.model.Payment
import com.gpch.hotel.repository.PaymentRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import java.time.LocalDate

class PaymentServiceTest {

    private lateinit var paymentRepository: PaymentRepository
    private lateinit var paymentService: PaymentService

    @BeforeEach
    fun setUp() {
        paymentRepository = mock(PaymentRepository::class.java)
        paymentService = PaymentService(paymentRepository)
    }

    @Test
    fun `save defaults payment date when missing`() {
        val today = LocalDate.now()
        val payment = payment(bookingId = 1L, amount = 250)
        `when`(paymentRepository.save(payment)).thenReturn(payment)

        val saved = paymentService.save(payment)

        assertEquals(today, saved.paymentDate)
        verify(paymentRepository).save(payment)
    }

    @Test
    fun `save rejects non positive payment amount`() {
        val payment = payment(bookingId = 1L, amount = 0)

        assertThrows(IllegalArgumentException::class.java) {
            paymentService.save(payment)
        }
    }

    @Test
    fun `total paid sums payments for selected booking`() {
        `when`(paymentRepository.totalPaidForBooking(5L)).thenReturn(150)

        val total = paymentService.totalPaidForBooking(5L)

        assertEquals(150, total)
    }

    @Test
    fun `total paid for bookings returns grouped totals`() {
        `when`(paymentRepository.totalPaidForBookings(listOf(5L, 7L))).thenReturn(
            listOf(
                arrayOf(5L, 150),
                arrayOf(7L, 90)
            )
        )

        val totals = paymentService.totalPaidForBookings(listOf(5L, 7L))

        assertEquals(mapOf(5L to 150, 7L to 90), totals)
    }

    @Test
    fun `total paid for bookings skips repository call when ids are empty`() {
        val totals = paymentService.totalPaidForBookings(emptyList())

        assertEquals(emptyMap<Long, Int>(), totals)
        verifyNoInteractions(paymentRepository)
    }

    private fun payment(id: Long = 0, bookingId: Long, amount: Int): Payment {
        val booking = Booking()
        booking.id = bookingId
        val payment = Payment()
        payment.id = id
        payment.booking = booking
        payment.amount = amount
        return payment
    }
}

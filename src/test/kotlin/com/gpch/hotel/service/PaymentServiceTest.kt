package com.gpch.hotel.service

import com.gpch.hotel.model.Booking
import com.gpch.hotel.model.Payment
import com.gpch.hotel.repository.PaymentRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
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
        val payment = payment(bookingId = 1L, amount = 250)
        `when`(paymentRepository.save(payment)).thenReturn(payment)

        val saved = paymentService.save(payment)

        assertEquals(LocalDate.now(), saved.paymentDate)
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
        `when`(paymentRepository.findAll()).thenReturn(
            listOf(
                payment(id = 1L, bookingId = 5L, amount = 100),
                payment(id = 2L, bookingId = 5L, amount = 50),
                payment(id = 3L, bookingId = 6L, amount = 90)
            )
        )

        val total = paymentService.totalPaidForBooking(5L)

        assertEquals(150, total)
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

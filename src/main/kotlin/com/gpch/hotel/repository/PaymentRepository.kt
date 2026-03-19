package com.gpch.hotel.repository

import com.gpch.hotel.model.Payment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository("paymentRepository")
interface PaymentRepository : JpaRepository<Payment, Long>

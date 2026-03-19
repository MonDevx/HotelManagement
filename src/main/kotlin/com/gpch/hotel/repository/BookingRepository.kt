package com.gpch.hotel.repository

import com.gpch.hotel.model.Booking
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository("bookingRepository")
interface BookingRepository : JpaRepository<Booking, Long>

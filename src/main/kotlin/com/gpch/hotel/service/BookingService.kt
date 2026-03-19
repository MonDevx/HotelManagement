package com.gpch.hotel.service

import com.gpch.hotel.model.Booking
import com.gpch.hotel.model.Room
import com.gpch.hotel.model.RoomType
import com.gpch.hotel.repository.BookingRepository
import com.gpch.hotel.repository.RoomRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Service("bookingService")
@Transactional
class BookingService(
    private val bookingRepository: BookingRepository,
    private val roomRepository: RoomRepository
) {

    companion object {
        val blockingStatuses = setOf("pending", "confirmed", "checked-in")
        val roomStatuses = listOf("available", "booked", "checked-in", "maintenance")
        val bookingStatuses = listOf("pending", "confirmed", "checked-in", "checked-out", "cancelled", "no-show")
    }

    fun findAll(): List<Booking> = bookingRepository.findAll().sortedWith(
        compareByDescending<Booking> { it.checkInDate ?: LocalDate.MIN }.thenByDescending { it.id }
    )

    fun findById(id: Long): Booking? = bookingRepository.findById(id).orElse(null)

    fun deleteById(id: Long) = bookingRepository.deleteById(id)

    fun findAvailableRooms(checkInDate: LocalDate, checkOutDate: LocalDate, roomTypeId: Long? = null, excludeBookingId: Long = 0): List<Room> {
        validateDates(checkInDate, checkOutDate)
        val blockingBookings = findBlockingBookings(excludeBookingId)
        return roomRepository.findAll()
            .filter { roomTypeId == null || it.roomType?.id == roomTypeId }
            .filterNot { it.status.equals("maintenance", ignoreCase = true) }
            .filter { room -> isRoomAvailable(room, checkInDate, checkOutDate, blockingBookings) }
            .sortedBy { it.roomNumber ?: "" }
    }

    fun save(booking: Booking): Booking {
        val checkInDate = booking.checkInDate ?: throw IllegalArgumentException("Check-in date is required")
        val checkOutDate = booking.checkOutDate ?: throw IllegalArgumentException("Check-out date is required")
        validateDates(checkInDate, checkOutDate)
        require(booking.guest != null) { "Guest is required" }
        require(booking.guestCount > 0) { "Guest count must be greater than zero" }

        val resolvedRoom = resolveRoom(booking, checkInDate, checkOutDate)
        val roomType = resolvedRoom.roomType ?: booking.roomType ?: throw IllegalArgumentException("Room type is required")
        require(booking.guestCount <= roomType.capacity) { "Guest count exceeds room capacity" }

        booking.room = resolvedRoom
        booking.roomType = roomType
        booking.totalPrice = calculateTotalPrice(roomType, checkInDate, checkOutDate, booking.discountPercent, booking.promotionDiscount) + booking.additionalCharges
        booking.status = normalizeStatus(booking.status, booking.walkIn)

        val existing = if (booking.id != 0L) findById(booking.id) else null
        val saved = bookingRepository.save(booking)
        if (existing?.room?.id != null && existing.room?.id != saved.room?.id && existing.room?.status != "maintenance") {
            existing.room?.status = "available"
            roomRepository.save(existing.room!!)
        }
        updateRoomStatus(saved.room, saved.status)
        return saved
    }

    fun cancelBooking(id: Long): Booking = updateStatus(id, "cancelled")

    fun markNoShow(id: Long): Booking = updateStatus(id, "no-show")

    fun checkIn(id: Long): Booking = updateStatus(id, "checked-in")

    fun checkOut(id: Long): Booking = updateStatus(id, "checked-out")

    fun countByStatus(status: String): Long = bookingRepository.findAll().count { it.status.equals(status, ignoreCase = true) }.toLong()

    fun bookingsBetween(start: LocalDate, end: LocalDate): List<Booking> = bookingRepository.findAll().filter {
        val checkIn = it.checkInDate
        checkIn != null && (checkIn.isEqual(start) || checkIn.isAfter(start)) && (checkIn.isEqual(end) || checkIn.isBefore(end))
    }

    fun calculateTotalPrice(
        roomType: RoomType,
        checkInDate: LocalDate,
        checkOutDate: LocalDate,
        discountPercent: Int,
        promotionDiscount: Int
    ): Int {
        validateDates(checkInDate, checkOutDate)
        require(discountPercent in 0..100) { "Discount percent must be between 0 and 100" }
        require(promotionDiscount >= 0) { "Promotion discount must not be negative" }
        val nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate).toInt().coerceAtLeast(1)
        val baseTotal = nights * roomType.price
        val discountAmount = baseTotal * discountPercent / 100
        return (baseTotal - discountAmount - promotionDiscount).coerceAtLeast(0)
    }

    private fun resolveRoom(booking: Booking, checkInDate: LocalDate, checkOutDate: LocalDate): Room {
        val preferredRoom = booking.room
        val blockingBookings = findBlockingBookings(booking.id)
        if (preferredRoom?.id != null && preferredRoom.id != 0L) {
            val room = roomRepository.findById(preferredRoom.id).orElse(null)
                ?: throw IllegalArgumentException("Selected room was not found")
            if (!isRoomAvailable(room, checkInDate, checkOutDate, blockingBookings)) {
                throw IllegalArgumentException("Selected room is not available for the requested dates")
            }
            if (booking.roomType == null) {
                booking.roomType = room.roomType
            }
            return room
        }

        val roomTypeId = booking.roomType?.id ?: throw IllegalArgumentException("Room type is required")
        return roomRepository.findAll()
            .filter { it.roomType?.id == roomTypeId }
            .filterNot { it.status.equals("maintenance", ignoreCase = true) }
            .filter { room -> isRoomAvailable(room, checkInDate, checkOutDate, blockingBookings) }
            .sortedBy { it.roomNumber ?: "" }
            .firstOrNull()
            ?: throw IllegalArgumentException("No rooms are available for the selected type and dates")
    }

    private fun findBlockingBookings(excludeBookingId: Long): List<Booking> = bookingRepository.findAll()
        .filter { it.id != excludeBookingId }
        .filter { it.room?.id != null }
        .filter { blockingStatuses.contains(it.status.lowercase()) }

    private fun isRoomAvailable(room: Room, checkInDate: LocalDate, checkOutDate: LocalDate, blockingBookings: List<Booking>): Boolean {
        return blockingBookings
            .filter { it.room?.id == room.id }
            .none {
                val existingCheckIn = it.checkInDate ?: return@none false
                val existingCheckOut = it.checkOutDate ?: return@none false
                checkInDate.isBefore(existingCheckOut) && checkOutDate.isAfter(existingCheckIn)
            }
    }

    private fun normalizeStatus(status: String?, walkIn: Boolean): String {
        val trimmed = status?.trim()?.lowercase()
        return when {
            walkIn -> "checked-in"
            trimmed.isNullOrBlank() -> "confirmed"
            trimmed !in bookingStatuses -> "confirmed"
            else -> trimmed
        }
    }

    private fun updateStatus(id: Long, status: String): Booking {
        val booking = findById(id) ?: throw IllegalArgumentException("Booking not found")
        booking.status = status
        if (status == "checked-out") {
            booking.room?.status = "available"
        } else if (status == "cancelled" || status == "no-show") {
            booking.room?.status = "available"
        } else if (status == "checked-in") {
            booking.room?.status = "checked-in"
        } else if (status == "confirmed" || status == "pending") {
            booking.room?.status = "booked"
        }
        booking.room?.let { roomRepository.save(it) }
        return bookingRepository.save(booking)
    }

    private fun updateRoomStatus(room: Room?, status: String) {
        if (room == null) return
        if (room.status == "maintenance") return
        room.status = when (status) {
            "checked-in" -> "checked-in"
            "cancelled", "checked-out", "no-show" -> "available"
            else -> "booked"
        }
        roomRepository.save(room)
    }

    private fun validateDates(checkInDate: LocalDate, checkOutDate: LocalDate) {
        require(checkOutDate.isAfter(checkInDate)) { "Check-out date must be after check-in date" }
    }
}

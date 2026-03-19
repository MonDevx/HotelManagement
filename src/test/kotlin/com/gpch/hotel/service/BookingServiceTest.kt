package com.gpch.hotel.service

import com.gpch.hotel.model.Booking
import com.gpch.hotel.model.Guest
import com.gpch.hotel.model.Room
import com.gpch.hotel.model.RoomType
import com.gpch.hotel.repository.BookingRepository
import com.gpch.hotel.repository.RoomRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import java.time.LocalDate
import java.util.Optional

class BookingServiceTest {

    private lateinit var bookingRepository: BookingRepository
    private lateinit var roomRepository: RoomRepository
    private lateinit var bookingService: BookingService

    @BeforeEach
    fun setUp() {
        bookingRepository = mock(BookingRepository::class.java)
        roomRepository = mock(RoomRepository::class.java)
        bookingService = BookingService(bookingRepository, roomRepository)
    }

    @Test
    fun `save assigns first available room and calculates total price`() {
        val roomType = roomType(1L, capacity = 2, price = 100)
        val room = room(10L, "101", roomType, status = "available")
        val booking = booking(roomType = roomType, room = null, discountPercent = 10, promotionDiscount = 5)
        `when`(roomRepository.findAll()).thenReturn(listOf(room))
        `when`(bookingRepository.findAll()).thenReturn(emptyList())
        `when`(bookingRepository.save(booking)).thenReturn(booking)
        `when`(roomRepository.save(room)).thenReturn(room)

        val saved = bookingService.save(booking)

        assertEquals(room, saved.room)
        assertEquals(175, saved.totalPrice)
        assertEquals("pending", saved.status)
        assertEquals("booked", room.status)
        verify(bookingRepository).save(booking)
        verify(roomRepository).save(room)
    }

    @Test
    fun `save rejects overlapping booking`() {
        val roomType = roomType(1L)
        val room = room(10L, "101", roomType)
        val existing = booking(id = 3L, roomType = roomType, room = room)
        val booking = booking(roomType = roomType, room = room)
        `when`(roomRepository.findById(10L)).thenReturn(Optional.of(room))
        `when`(bookingRepository.findAll()).thenReturn(listOf(existing))

        assertThrows(IllegalArgumentException::class.java) {
            bookingService.save(booking)
        }
    }

    @Test
    fun `save walk in booking checks guest in immediately`() {
        val roomType = roomType(1L, capacity = 2, price = 100)
        val room = room(10L, "101", roomType)
        val booking = booking(roomType = roomType, room = room, walkIn = true)
        `when`(roomRepository.findById(10L)).thenReturn(Optional.of(room))
        `when`(bookingRepository.findAll()).thenReturn(emptyList())
        `when`(bookingRepository.save(booking)).thenReturn(booking)
        `when`(roomRepository.save(room)).thenReturn(room)

        val saved = bookingService.save(booking)

        assertEquals("checked-in", saved.status)
        assertEquals("checked-in", room.status)
    }

    @Test
    fun `check out releases room`() {
        val roomType = roomType(1L)
        val room = room(11L, "102", roomType, status = "checked-in")
        val booking = booking(id = 5L, roomType = roomType, room = room, status = "checked-in")
        `when`(bookingRepository.findById(5L)).thenReturn(Optional.of(booking))
        `when`(bookingRepository.save(booking)).thenReturn(booking)
        `when`(roomRepository.save(room)).thenReturn(room)

        val updated = bookingService.checkOut(5L)

        assertEquals("checked-out", updated.status)
        assertEquals("available", room.status)
    }

    private fun booking(
        id: Long = 0,
        roomType: RoomType,
        room: Room? = null,
        status: String = "pending",
        walkIn: Boolean = false,
        discountPercent: Int = 0,
        promotionDiscount: Int = 0
    ): Booking {
        val booking = Booking()
        booking.id = id
        booking.guest = guest(1L)
        booking.roomType = roomType
        booking.room = room
        booking.checkInDate = LocalDate.of(2026, 3, 20)
        booking.checkOutDate = LocalDate.of(2026, 3, 22)
        booking.guestCount = 2
        booking.status = status
        booking.walkIn = walkIn
        booking.discountPercent = discountPercent
        booking.promotionDiscount = promotionDiscount
        return booking
    }

    private fun guest(id: Long): Guest {
        val guest = Guest()
        guest.id = id
        guest.firstName = "Demo"
        guest.lastName = "Guest"
        return guest
    }

    private fun roomType(id: Long, capacity: Int = 2, price: Int = 100): RoomType {
        val roomType = RoomType()
        roomType.id = id
        roomType.name = "Deluxe"
        roomType.capacity = capacity
        roomType.price = price
        return roomType
    }

    private fun room(id: Long, number: String, roomType: RoomType, status: String = "available"): Room {
        val room = Room()
        room.id = id
        room.roomNumber = number
        room.roomType = roomType
        room.status = status
        return room
    }
}

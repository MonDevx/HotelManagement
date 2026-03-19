package com.gpch.hotel.repository

import com.gpch.hotel.model.Room
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository("roomRepository")
interface RoomRepository : JpaRepository<Room, Long> {
    fun findAllByRoomTypeId(roomTypeId: Long): List<Room>
    fun countByStatus(status: String): Long
}

package com.gpch.hotel.service

import com.gpch.hotel.model.Room
import com.gpch.hotel.repository.RoomRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service("roomService")
@Transactional
class RoomService(private val roomRepository: RoomRepository) {

    fun findAll(): List<Room> = roomRepository.findAll()

    fun findById(id: Long): Room? = roomRepository.findById(id).orElse(null)

    fun save(room: Room) = roomRepository.save(room)

    fun deleteById(id: Long) = roomRepository.deleteById(id)

    fun findAllByRoomType(roomTypeId: Long): List<Room> = roomRepository.findAllByRoomTypeId(roomTypeId)

    fun countByStatus(status: String): Long = roomRepository.countByStatus(status)
}

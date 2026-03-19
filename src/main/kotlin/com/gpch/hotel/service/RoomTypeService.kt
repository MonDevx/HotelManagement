package com.gpch.hotel.service

import com.gpch.hotel.model.RoomType
import com.gpch.hotel.repository.RoomTypeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service("roomTypeService")
@Transactional
class RoomTypeService(private val roomTypeRepository: RoomTypeRepository) {

    fun findAll(): List<RoomType> = roomTypeRepository.findAll()

    fun findById(id: Long): RoomType? = roomTypeRepository.findById(id).orElse(null)

    fun save(roomType: RoomType) = roomTypeRepository.save(roomType)

    fun deleteById(id: Long) = roomTypeRepository.deleteById(id)
}

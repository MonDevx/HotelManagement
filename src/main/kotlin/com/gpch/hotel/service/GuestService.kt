package com.gpch.hotel.service

import com.gpch.hotel.model.Guest
import com.gpch.hotel.repository.GuestRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service("guestService")
@Transactional
class GuestService(private val guestRepository: GuestRepository) {

    fun findAll(): List<Guest> = guestRepository.findAll()

    fun search(keyword: String?): List<Guest> = if (keyword.isNullOrBlank()) findAll() else guestRepository.search(keyword.trim())

    fun findById(id: Long): Guest? = guestRepository.findById(id).orElse(null)

    fun save(guest: Guest) = guestRepository.save(guest)

    fun deleteById(id: Long) = guestRepository.deleteById(id)
}

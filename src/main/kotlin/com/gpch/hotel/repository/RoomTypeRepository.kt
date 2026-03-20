package com.gpch.hotel.repository

import com.gpch.hotel.model.RoomType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository("roomTypeRepository")
interface RoomTypeRepository : JpaRepository<RoomType, Long>

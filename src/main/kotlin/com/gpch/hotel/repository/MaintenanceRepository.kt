package com.gpch.hotel.repository

import com.gpch.hotel.model.Maintenance
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository("maintenanceRepository")
interface MaintenanceRepository : JpaRepository<Maintenance, Long> {

    fun countByStatus(status: String): Long
}

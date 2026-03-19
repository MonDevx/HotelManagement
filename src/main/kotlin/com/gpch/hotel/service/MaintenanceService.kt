package com.gpch.hotel.service

import com.gpch.hotel.model.Maintenance
import com.gpch.hotel.repository.MaintenanceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.Date
import java.util.Calendar

@Service("maintenanceService")
@Transactional
class MaintenanceService @Autowired constructor(
    @Qualifier("maintenanceRepository") private val maintenanceRepository: MaintenanceRepository
) {

    fun saveMaintenance(maintenance: Maintenance) {
        val currentTime = Calendar.getInstance()
        maintenance.date = Date(currentTime.time.time)
        maintenance.status = "Not_yet_implemented"
        maintenanceRepository.save(maintenance)
    }

    fun findAll(): List<Maintenance> = maintenanceRepository.findAll()

    fun deleteMaintenanceById(id: Long) = maintenanceRepository.deleteById(id)

    fun findMaintenanceById(id: Long): Maintenance? = maintenanceRepository.findById(id).orElse(null)

    fun updateMaintenance(maintenance: Maintenance) {
        val maintenanceFromDb = maintenanceRepository.findById(maintenance.id).orElse(null)!!
        maintenanceFromDb.status = maintenance.status
        maintenanceRepository.save(maintenanceFromDb)
    }
}

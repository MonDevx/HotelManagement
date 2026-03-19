package com.gpch.hotel.service

import com.gpch.hotel.model.Position
import com.gpch.hotel.repository.EmployeeRepository
import com.gpch.hotel.repository.MaintenanceRepository
import com.gpch.hotel.repository.PositionRepository
import com.gpch.hotel.repository.StoreRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service("dashboardService")
@Transactional
class DashboardService @Autowired constructor(
    private val maintenanceRepository: MaintenanceRepository,
    private val employeeRepository: EmployeeRepository,
    private val storeRepository: StoreRepository,
    private val positionRepository: PositionRepository
) {

    fun countEmployees(): Long = employeeRepository.count()

    fun countPosition(position: Position): Long = employeeRepository.countByPositions(position)

    fun countStores(): Long = storeRepository.count()

    fun sumSalary(): Int? = employeeRepository.salaryTotals()

    fun countMaintenance(): Long = maintenanceRepository.countByStatus("Not_yet_implemented")

    fun findAllPosition(): List<Position> = positionRepository.findAll()
}

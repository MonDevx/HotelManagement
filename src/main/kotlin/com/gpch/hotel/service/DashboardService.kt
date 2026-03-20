package com.gpch.hotel.service

import com.gpch.hotel.model.Position
import com.gpch.hotel.repository.BookingRepository
import com.gpch.hotel.repository.EmployeeRepository
import com.gpch.hotel.repository.MaintenanceRepository
import com.gpch.hotel.repository.PaymentRepository
import com.gpch.hotel.repository.PositionRepository
import com.gpch.hotel.repository.RoomRepository
import com.gpch.hotel.repository.RoomTypeRepository
import com.gpch.hotel.repository.StoreRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service("dashboardService")
@Transactional
class DashboardService @Autowired constructor(
    private val maintenanceRepository: MaintenanceRepository,
    private val employeeRepository: EmployeeRepository,
    private val storeRepository: StoreRepository,
    private val positionRepository: PositionRepository,
    private val roomTypeRepository: RoomTypeRepository,
    private val roomRepository: RoomRepository,
    private val bookingRepository: BookingRepository,
    private val paymentRepository: PaymentRepository
) {

    fun countEmployees(): Long = employeeRepository.count()

    fun countPosition(position: Position): Long = employeeRepository.countByPositions(position)

    fun countStores(): Long = storeRepository.count()

    fun sumSalary(): Int? = employeeRepository.salaryTotals()

    fun countMaintenance(): Long = maintenanceRepository.countByStatus("Not_yet_implemented")

    fun findAllPosition(): List<Position> = positionRepository.findAll()

    fun countRoomTypes(): Long = roomTypeRepository.count()

    fun countRooms(): Long = roomRepository.count()

    fun countAvailableRooms(): Long = roomRepository.countByStatusIgnoreCase("available")

    fun countActiveStays(): Long = bookingRepository.countByStatusIgnoreCase("checked-in")

    fun occupancyRate(): Int {
        val totalRooms = countRooms()
        if (totalRooms == 0L) {
            return 0
        }
        return ((countActiveStays().toDouble() / totalRooms.toDouble()) * 100).toInt()
    }

    fun countBookingsToday(): Long = bookingsFor(LocalDate.now(), LocalDate.now())

    fun countBookingsThisMonth(): Long {
        val today = LocalDate.now()
        return bookingsFor(today.withDayOfMonth(1), today.withDayOfMonth(today.lengthOfMonth()))
    }

    fun countBookingsThisYear(): Long {
        val today = LocalDate.now()
        return bookingsFor(today.withDayOfYear(1), today.withDayOfYear(today.lengthOfYear()))
    }

    fun revenueToday(): Int = revenueFor(LocalDate.now(), LocalDate.now())

    fun revenueThisMonth(): Int {
        val today = LocalDate.now()
        return revenueFor(today.withDayOfMonth(1), today.withDayOfMonth(today.lengthOfMonth()))
    }

    fun revenueThisYear(): Int {
        val today = LocalDate.now()
        return revenueFor(today.withDayOfYear(1), today.withDayOfYear(today.lengthOfYear()))
    }

    private fun bookingsFor(start: LocalDate, end: LocalDate): Long = bookingRepository.countByCheckInDateBetween(start, end)

    private fun revenueFor(start: LocalDate, end: LocalDate): Int = paymentRepository.sumAmountBetween(start, end)
}

package com.gpch.hotel.service

import com.gpch.hotel.repository.BookingRepository
import com.gpch.hotel.repository.EmployeeRepository
import com.gpch.hotel.repository.MaintenanceRepository
import com.gpch.hotel.repository.PaymentRepository
import com.gpch.hotel.repository.PositionRepository
import com.gpch.hotel.repository.RoomRepository
import com.gpch.hotel.repository.RoomTypeRepository
import com.gpch.hotel.repository.StoreRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class DashboardServiceTest {

    private lateinit var maintenanceRepository: MaintenanceRepository
    private lateinit var employeeRepository: EmployeeRepository
    private lateinit var storeRepository: StoreRepository
    private lateinit var positionRepository: PositionRepository
    private lateinit var roomTypeRepository: RoomTypeRepository
    private lateinit var roomRepository: RoomRepository
    private lateinit var bookingRepository: BookingRepository
    private lateinit var paymentRepository: PaymentRepository
    private lateinit var dashboardService: DashboardService

    @BeforeEach
    fun setUp() {
        maintenanceRepository = mock(MaintenanceRepository::class.java)
        employeeRepository = mock(EmployeeRepository::class.java)
        storeRepository = mock(StoreRepository::class.java)
        positionRepository = mock(PositionRepository::class.java)
        roomTypeRepository = mock(RoomTypeRepository::class.java)
        roomRepository = mock(RoomRepository::class.java)
        bookingRepository = mock(BookingRepository::class.java)
        paymentRepository = mock(PaymentRepository::class.java)
        dashboardService = DashboardService(
            maintenanceRepository,
            employeeRepository,
            storeRepository,
            positionRepository,
            roomTypeRepository,
            roomRepository,
            bookingRepository,
            paymentRepository
        )
    }

    @Test
    fun `count available rooms uses repository count query`() {
        `when`(roomRepository.countByStatusIgnoreCase("available")).thenReturn(8)

        val count = dashboardService.countAvailableRooms()

        assertEquals(8, count)
        verify(roomRepository).countByStatusIgnoreCase("available")
    }

    @Test
    fun `count active stays uses repository count query`() {
        `when`(bookingRepository.countByStatusIgnoreCase("checked-in")).thenReturn(3)

        val count = dashboardService.countActiveStays()

        assertEquals(3, count)
        verify(bookingRepository).countByStatusIgnoreCase("checked-in")
    }
}

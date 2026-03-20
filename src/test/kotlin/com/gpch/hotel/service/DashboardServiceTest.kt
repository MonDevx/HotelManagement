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
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.`when`
import java.time.LocalDate

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

    @Test
    fun `occupancy rate returns zero when there are no rooms`() {
        `when`(roomRepository.count()).thenReturn(0)

        val occupancyRate = dashboardService.occupancyRate()

        assertEquals(0, occupancyRate)
        verify(roomRepository).count()
        verifyNoInteractions(bookingRepository)
    }

    @Test
    fun `occupancy rate uses checked in stays against total rooms`() {
        `when`(roomRepository.count()).thenReturn(5)
        `when`(bookingRepository.countByStatusIgnoreCase("checked-in")).thenReturn(2)

        val occupancyRate = dashboardService.occupancyRate()

        assertEquals(40, occupancyRate)
        verify(roomRepository).count()
        verify(bookingRepository).countByStatusIgnoreCase("checked-in")
    }

    @Test
    fun `count bookings today queries today's range`() {
        val today = LocalDate.now()
        `when`(bookingRepository.countByCheckInDateBetween(today, today)).thenReturn(6)

        val count = dashboardService.countBookingsToday()

        assertEquals(6, count)
        verify(bookingRepository).countByCheckInDateBetween(today, today)
    }

    @Test
    fun `count bookings this month queries first and last day of month`() {
        val today = LocalDate.now()
        val start = today.withDayOfMonth(1)
        val end = today.withDayOfMonth(today.lengthOfMonth())
        `when`(bookingRepository.countByCheckInDateBetween(start, end)).thenReturn(12)

        val count = dashboardService.countBookingsThisMonth()

        assertEquals(12, count)
        verify(bookingRepository).countByCheckInDateBetween(start, end)
    }

    @Test
    fun `count bookings this year queries first and last day of year`() {
        val today = LocalDate.now()
        val start = today.withDayOfYear(1)
        val end = today.withDayOfYear(today.lengthOfYear())
        `when`(bookingRepository.countByCheckInDateBetween(start, end)).thenReturn(42)

        val count = dashboardService.countBookingsThisYear()

        assertEquals(42, count)
        verify(bookingRepository).countByCheckInDateBetween(start, end)
    }

    @Test
    fun `revenue today queries today's range`() {
        val today = LocalDate.now()
        `when`(paymentRepository.sumAmountBetween(today, today)).thenReturn(320)

        val total = dashboardService.revenueToday()

        assertEquals(320, total)
        verify(paymentRepository).sumAmountBetween(today, today)
    }

    @Test
    fun `revenue this month queries first and last day of month`() {
        val today = LocalDate.now()
        val start = today.withDayOfMonth(1)
        val end = today.withDayOfMonth(today.lengthOfMonth())
        `when`(paymentRepository.sumAmountBetween(start, end)).thenReturn(1600)

        val total = dashboardService.revenueThisMonth()

        assertEquals(1600, total)
        verify(paymentRepository).sumAmountBetween(start, end)
    }

    @Test
    fun `revenue this year queries first and last day of year`() {
        val today = LocalDate.now()
        val start = today.withDayOfYear(1)
        val end = today.withDayOfYear(today.lengthOfYear())
        `when`(paymentRepository.sumAmountBetween(start, end)).thenReturn(9200)

        val total = dashboardService.revenueThisYear()

        assertEquals(9200, total)
        verify(paymentRepository).sumAmountBetween(start, end)
    }
}

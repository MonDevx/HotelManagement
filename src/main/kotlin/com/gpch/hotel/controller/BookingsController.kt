package com.gpch.hotel.controller

import com.gpch.hotel.model.Booking
import com.gpch.hotel.service.BookingService
import com.gpch.hotel.service.GuestService
import com.gpch.hotel.service.PaymentService
import com.gpch.hotel.service.RoomService
import com.gpch.hotel.service.RoomTypeService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.time.LocalDate

@Controller
@RequestMapping("bookings")
class BookingsController(
    private val bookingService: BookingService,
    private val guestService: GuestService,
    private val roomTypeService: RoomTypeService,
    private val roomService: RoomService,
    private val paymentService: PaymentService
) {

    @GetMapping("manage-bookings")
    fun manage(
        @RequestParam(required = false) editId: Long?,
        @RequestParam(required = false) availabilityCheckIn: String?,
        @RequestParam(required = false) availabilityCheckOut: String?,
        @RequestParam(required = false) availabilityRoomTypeId: Long?
    ): ModelAndView {
        val modelAndView = ModelAndView("bookings/manage-bookings")
        val bookings = bookingService.findAll()
        val availableRooms = if (!availabilityCheckIn.isNullOrBlank() && !availabilityCheckOut.isNullOrBlank()) {
            try {
                bookingService.findAvailableRooms(
                    LocalDate.parse(availabilityCheckIn),
                    LocalDate.parse(availabilityCheckOut),
                    availabilityRoomTypeId
                )
            } catch (ex: RuntimeException) {
                emptyList()
            }
        } else {
            emptyList()
        }
        modelAndView.addObject("booking", editId?.let { bookingService.findById(it) } ?: Booking())
        modelAndView.addObject("list", bookings)
        modelAndView.addObject("guests", guestService.findAll())
        modelAndView.addObject("roomTypes", roomTypeService.findAll())
        modelAndView.addObject("rooms", roomService.findAll())
        modelAndView.addObject("statuses", BookingService.bookingStatuses)
        modelAndView.addObject("availableRooms", availableRooms)
        modelAndView.addObject("paidTotals", paymentService.totalPaidForBookings(bookings.map { it.id }.filter { it != 0L }))
        modelAndView.addObject("availabilityCheckIn", availabilityCheckIn ?: "")
        modelAndView.addObject("availabilityCheckOut", availabilityCheckOut ?: "")
        modelAndView.addObject("availabilityRoomTypeId", availabilityRoomTypeId)
        return modelAndView
    }

    @PostMapping("save")
    fun save(@ModelAttribute booking: Booking, redirectAttributes: RedirectAttributes): String {
        return try {
            bookingService.save(booking)
            redirectAttributes.addFlashAttribute("message", "Booking saved successfully")
            "redirect:/bookings/manage-bookings"
        } catch (ex: IllegalArgumentException) {
            redirectAttributes.addFlashAttribute("message_error", ex.message)
            "redirect:/bookings/manage-bookings"
        }
    }

    @GetMapping("delete")
    fun delete(@RequestParam id: Long, redirectAttributes: RedirectAttributes): String {
        bookingService.deleteById(id)
        redirectAttributes.addFlashAttribute("message", "Booking deleted successfully")
        return "redirect:/bookings/manage-bookings"
    }

    @GetMapping("cancel")
    fun cancel(@RequestParam id: Long, redirectAttributes: RedirectAttributes): String {
        bookingService.cancelBooking(id)
        redirectAttributes.addFlashAttribute("message", "Booking cancelled successfully")
        return "redirect:/bookings/manage-bookings"
    }

    @GetMapping("no-show")
    fun noShow(@RequestParam id: Long, redirectAttributes: RedirectAttributes): String {
        bookingService.markNoShow(id)
        redirectAttributes.addFlashAttribute("message", "Booking marked as no-show")
        return "redirect:/bookings/manage-bookings"
    }

    @GetMapping("check-in")
    fun checkIn(@RequestParam id: Long, redirectAttributes: RedirectAttributes): String {
        bookingService.checkIn(id)
        redirectAttributes.addFlashAttribute("message", "Guest checked in successfully")
        return "redirect:/bookings/manage-bookings"
    }

    @GetMapping("check-out")
    fun checkOut(@RequestParam id: Long, redirectAttributes: RedirectAttributes): String {
        bookingService.checkOut(id)
        redirectAttributes.addFlashAttribute("message", "Guest checked out successfully")
        return "redirect:/bookings/manage-bookings"
    }
}

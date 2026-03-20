package com.gpch.hotel.controller

import com.gpch.hotel.model.Room
import com.gpch.hotel.service.BookingService
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

@Controller
@RequestMapping("rooms")
class RoomsController(
    private val roomService: RoomService,
    private val roomTypeService: RoomTypeService
) {

    @GetMapping("manage-rooms")
    fun manage(@RequestParam(required = false) editId: Long?): ModelAndView {
        val modelAndView = ModelAndView("rooms/manage-rooms")
        modelAndView.addObject("room", editId?.let { roomService.findById(it) } ?: Room())
        modelAndView.addObject("list", roomService.findAll())
        modelAndView.addObject("roomTypes", roomTypeService.findAll())
        modelAndView.addObject("statuses", BookingService.roomStatuses)
        return modelAndView
    }

    @PostMapping("save")
    fun save(@ModelAttribute room: Room, redirectAttributes: RedirectAttributes): String {
        roomService.save(room)
        redirectAttributes.addFlashAttribute("message", "Room saved successfully")
        return "redirect:/rooms/manage-rooms"
    }

    @GetMapping("delete")
    fun delete(@RequestParam id: Long, redirectAttributes: RedirectAttributes): String {
        roomService.deleteById(id)
        redirectAttributes.addFlashAttribute("message", "Room deleted successfully")
        return "redirect:/rooms/manage-rooms"
    }
}

package com.gpch.hotel.controller

import com.gpch.hotel.model.RoomType
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
@RequestMapping("room-types")
class RoomTypeController(private val roomTypeService: RoomTypeService) {

    @GetMapping("manage-room-types")
    fun manage(@RequestParam(required = false) editId: Long?): ModelAndView {
        val modelAndView = ModelAndView("roomtypes/manage-room-types")
        modelAndView.addObject("roomType", editId?.let { roomTypeService.findById(it) } ?: RoomType())
        modelAndView.addObject("list", roomTypeService.findAll())
        return modelAndView
    }

    @PostMapping("save")
    fun save(@ModelAttribute roomType: RoomType, redirectAttributes: RedirectAttributes): String {
        roomTypeService.save(roomType)
        redirectAttributes.addFlashAttribute("message", "Room type saved successfully")
        return "redirect:/room-types/manage-room-types"
    }

    @GetMapping("delete")
    fun delete(@RequestParam id: Long, redirectAttributes: RedirectAttributes): String {
        roomTypeService.deleteById(id)
        redirectAttributes.addFlashAttribute("message", "Room type deleted successfully")
        return "redirect:/room-types/manage-room-types"
    }
}

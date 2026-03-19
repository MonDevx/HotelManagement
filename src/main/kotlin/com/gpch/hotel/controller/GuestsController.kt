package com.gpch.hotel.controller

import com.gpch.hotel.model.Guest
import com.gpch.hotel.service.GuestService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("guests")
class GuestsController(private val guestService: GuestService) {

    @GetMapping("manage-guests")
    fun manage(
        @RequestParam(required = false) editId: Long?,
        @RequestParam(required = false) keyword: String?
    ): ModelAndView {
        val modelAndView = ModelAndView("guests/manage-guests")
        modelAndView.addObject("guest", editId?.let { guestService.findById(it) } ?: Guest())
        modelAndView.addObject("list", guestService.search(keyword))
        modelAndView.addObject("keyword", keyword ?: "")
        return modelAndView
    }

    @PostMapping("save")
    fun save(@ModelAttribute guest: Guest, redirectAttributes: RedirectAttributes): String {
        guestService.save(guest)
        redirectAttributes.addFlashAttribute("message", "Guest saved successfully")
        return "redirect:/guests/manage-guests"
    }

    @GetMapping("delete")
    fun delete(@RequestParam id: Long, redirectAttributes: RedirectAttributes): String {
        guestService.deleteById(id)
        redirectAttributes.addFlashAttribute("message", "Guest deleted successfully")
        return "redirect:/guests/manage-guests"
    }
}

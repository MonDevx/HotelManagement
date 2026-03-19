package com.gpch.hotel.controller

import com.gpch.hotel.model.Payment
import com.gpch.hotel.service.BookingService
import com.gpch.hotel.service.PaymentService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("payments")
class PaymentsController(
    private val paymentService: PaymentService,
    private val bookingService: BookingService
) {

    @GetMapping("manage-payments")
    fun manage(@RequestParam(required = false) editId: Long?): ModelAndView {
        val modelAndView = ModelAndView("payments/manage-payments")
        modelAndView.addObject("payment", editId?.let { paymentService.findById(it) } ?: Payment())
        modelAndView.addObject("list", paymentService.findAll())
        modelAndView.addObject("bookings", bookingService.findAll())
        modelAndView.addObject("methods", listOf("cash", "bank-transfer", "credit-card"))
        modelAndView.addObject("paymentTypes", listOf("deposit", "balance"))
        modelAndView.addObject("paymentService", paymentService)
        return modelAndView
    }

    @PostMapping("save")
    fun save(@ModelAttribute payment: Payment, redirectAttributes: RedirectAttributes): String {
        return try {
            paymentService.save(payment)
            redirectAttributes.addFlashAttribute("message", "Payment saved successfully")
            "redirect:/payments/manage-payments"
        } catch (ex: IllegalArgumentException) {
            redirectAttributes.addFlashAttribute("message_error", ex.message)
            "redirect:/payments/manage-payments"
        }
    }

    @GetMapping("delete")
    fun delete(@RequestParam id: Long, redirectAttributes: RedirectAttributes): String {
        paymentService.deleteById(id)
        redirectAttributes.addFlashAttribute("message", "Payment deleted successfully")
        return "redirect:/payments/manage-payments"
    }
}

package com.gpch.hotel.controller

import com.gpch.hotel.model.Maintenance
import com.gpch.hotel.service.MaintenanceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.validation.Valid

@Controller
class MaintenanceController @Autowired constructor(
    private val maintenanceService: MaintenanceService,
    private val messageSource: MessageSource
) {

    @RequestMapping(value = ["maintenances/add-maintenance"], method = [RequestMethod.GET])
    fun maintenance(): ModelAndView {
        val modelAndView = ModelAndView()
        modelAndView.addObject("maintenance", Maintenance())
        modelAndView.viewName = "maintenances/add-maintenance"
        return modelAndView
    }

    @RequestMapping(value = ["maintenances/manage-maintenance"], method = [RequestMethod.GET])
    fun maintenances(): ModelAndView {
        val modelAndView = ModelAndView()
        modelAndView.addObject("list", maintenanceService.findAll())
        modelAndView.addObject("maintenance", Maintenance())
        modelAndView.viewName = "maintenances/manage-maintenance"
        return modelAndView
    }

    @RequestMapping(value = ["maintenances/add_maintenance"], method = [RequestMethod.POST])
    fun addMaintenance(
        @Valid maintenance: Maintenance,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes
    ): ModelAndView {
        val modelAndView = ModelAndView()
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("message_error", bindingResult.allErrors)
        } else {
            maintenanceService.saveMaintenance(maintenance)
            redirectAttributes.addFlashAttribute(
                "message",
                messageSource.getMessage("maintenance.a.s.text", null, LocaleContextHolder.getLocale())
            )
        }
        modelAndView.viewName = "redirect:/maintenances/add-maintenance"
        return modelAndView
    }

    @RequestMapping(value = ["maintenances/delete_maintenance{id}"], method = [RequestMethod.GET])
    fun deleteMaintenance(
        @RequestParam(name = "id") id: Long,
        redirectAttributes: RedirectAttributes
    ): String {
        maintenanceService.deleteMaintenanceById(id)
        redirectAttributes.addFlashAttribute(
            "message",
            messageSource.getMessage("maintenance.d.s.text", null, LocaleContextHolder.getLocale())
        )
        return "redirect:/maintenances/manage-maintenance"
    }

    @RequestMapping(value = ["maintenances/edit_maintenance{id}"], method = [RequestMethod.GET])
    @ResponseBody
    fun findMaintenance(@RequestParam(name = "id") id: Long): Maintenance? =
        maintenanceService.findMaintenanceById(id)

    @RequestMapping(value = ["maintenances/updatemaintenance{id}"], method = [RequestMethod.POST])
    fun updateMaintenance(
        @Valid maintenance: Maintenance,
        @RequestParam(name = "id") id: Long,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes
    ): ModelAndView {
        val modelAndView = ModelAndView()
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("message_error", bindingResult.allErrors)
        } else {
            maintenance.id = id
            maintenanceService.updateMaintenance(maintenance)
            redirectAttributes.addFlashAttribute(
                "message",
                messageSource.getMessage("maintenance.u.s.text", null, LocaleContextHolder.getLocale())
            )
        }
        modelAndView.viewName = "redirect:/maintenances/manage-maintenance"
        return modelAndView
    }
}

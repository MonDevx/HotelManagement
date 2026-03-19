package com.gpch.hotel.controller

import com.gpch.hotel.model.Store
import com.gpch.hotel.service.StoreService
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
class StoresController @Autowired constructor(
    private val storeService: StoreService,
    private val messageSource: MessageSource
) {

    @RequestMapping(value = ["stores/manage-stores"], method = [RequestMethod.GET])
    fun stores(): ModelAndView {
        val modelAndView = ModelAndView()
        val listStores = storeService.findAll()
        modelAndView.addObject("store", Store())
        modelAndView.addObject("list", listStores)
        modelAndView.viewName = "stores/manage-stores"
        return modelAndView
    }

    @RequestMapping(value = ["stores/delete_store{id}"], method = [RequestMethod.GET])
    fun deleteStore(
        @RequestParam(name = "id") id: Long,
        redirectAttributes: RedirectAttributes
    ): String {
        storeService.deleteStoreById(id)
        redirectAttributes.addFlashAttribute(
            "message",
            messageSource.getMessage("store.d.s.text", null, LocaleContextHolder.getLocale())
        )
        return "redirect:/stores/manage-stores"
    }

    @RequestMapping(value = ["stores/edit_store{id}"], method = [RequestMethod.GET])
    @ResponseBody
    fun findStore(@RequestParam(name = "id") id: Long): Store? = storeService.findStoreById(id)

    @RequestMapping(value = ["stores/updatestore{id}"], method = [RequestMethod.POST])
    fun updateStore(
        @Valid store: Store,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes,
        @RequestParam(name = "id") id: Long
    ): ModelAndView {
        val modelAndView = ModelAndView()
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("message_error", bindingResult.allErrors)
        } else {
            store.id = id
            storeService.updateStore(store)
            redirectAttributes.addFlashAttribute(
                "message",
                messageSource.getMessage("store.u.s.text", null, LocaleContextHolder.getLocale())
            )
        }
        modelAndView.viewName = "redirect:/stores/manage-stores"
        return modelAndView
    }

    @RequestMapping(value = ["stores/add_store"], method = [RequestMethod.POST])
    fun addStore(
        @Valid store: Store,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes
    ): ModelAndView {
        val modelAndView = ModelAndView()
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("message_error", bindingResult.allErrors)
        } else {
            storeService.saveStore(store)
            redirectAttributes.addFlashAttribute(
                "message",
                messageSource.getMessage("store.a.s.text", null, LocaleContextHolder.getLocale())
            )
        }
        modelAndView.viewName = "redirect:/stores/manage-stores"
        return modelAndView
    }
}

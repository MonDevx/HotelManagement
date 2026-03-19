package com.gpch.hotel.controller

import com.gpch.hotel.model.User
import com.gpch.hotel.service.UserService
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
class UsersController @Autowired constructor(
    private val userService: UserService,
    private val messageSource: MessageSource
) {

    @RequestMapping(value = ["users/manage-users"], method = [RequestMethod.GET])
    fun user(): ModelAndView {
        val modelAndView = ModelAndView()
        modelAndView.addObject("list", userService.findAll())
        modelAndView.addObject("user", User())
        modelAndView.viewName = "users/manage-users"
        return modelAndView
    }

    @RequestMapping(value = ["users/delete_user{id}"], method = [RequestMethod.GET])
    fun deleteUser(
        @RequestParam(name = "id") id: String,
        redirectAttributes: RedirectAttributes,
        bindingResult: BindingResult
    ): String {
        val modelAndView = ModelAndView()
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("message_error", bindingResult.allErrors)
        } else {
            userService.deleteUserById(id)
            redirectAttributes.addFlashAttribute(
                "message",
                messageSource.getMessage("user.d.s.text", null, LocaleContextHolder.getLocale())
            )
        }
        return "redirect:/users/manage-users"
    }

    @RequestMapping(value = ["users/edit_user{id}"], method = [RequestMethod.GET])
    @ResponseBody
    fun findUser(@RequestParam(name = "id") id: String): User? = userService.findUserById(id)

    @RequestMapping(value = ["users/editpassword_user"], method = [RequestMethod.POST])
    fun changePassword(
        redirectAttributes: RedirectAttributes,
        @RequestParam requestParams: Map<String, String>
    ): ModelAndView {
        val modelAndView = ModelAndView()
        val id = requestParams["id"]!!
        val newPassword = requestParams["newp"]
        val confirmNewPassword = requestParams["cnewp"]
        if (newPassword != confirmNewPassword) {
            redirectAttributes.addFlashAttribute(
                "message_error",
                messageSource.getMessage("cp.e.text", null, LocaleContextHolder.getLocale())
            )
        } else {
            userService.changePassword(id, newPassword!!)
            redirectAttributes.addFlashAttribute(
                "message",
                messageSource.getMessage("cp.u.s.text", null, LocaleContextHolder.getLocale())
            )
        }
        modelAndView.viewName = "redirect:/users/manage-users"
        return modelAndView
    }

    @RequestMapping(value = ["users/updateuser{id}"], method = [RequestMethod.POST])
    fun updateUser(
        @Valid user: User,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes,
        @RequestParam(name = "id") id: String
    ): ModelAndView {
        val modelAndView = ModelAndView()
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("message_error", bindingResult.allErrors)
        } else {
            user.id = id
            userService.updateUser(user)
            redirectAttributes.addFlashAttribute(
                "message",
                messageSource.getMessage("user.u.s.text", null, LocaleContextHolder.getLocale())
            )
        }
        modelAndView.viewName = "redirect:/users/manage-users"
        return modelAndView
    }

    @RequestMapping(value = ["users/add_user"], method = [RequestMethod.POST])
    fun createNewUser(
        @Valid user: User,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes
    ): ModelAndView {
        val modelAndView = ModelAndView()
        val userExists = userService.findUserByEmail(user.email!!)
        if (userExists != null) {
            bindingResult.rejectValue(
                "email", "public.user",
                messageSource.getMessage("user.a.a.e.e.text", null, LocaleContextHolder.getLocale())
            )
        }
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("list", userService.findAll())
            modelAndView.addObject("user", user)
            modelAndView.addObject(
                "message_error",
                messageSource.getMessage("user.a.a.e.text", null, LocaleContextHolder.getLocale())
            )
            modelAndView.viewName = "users/manage-users"
        } else {
            userService.saveUser(user)
            redirectAttributes.addFlashAttribute(
                "message",
                messageSource.getMessage("user.a.s.text", null, LocaleContextHolder.getLocale())
            )
            modelAndView.viewName = "redirect:/users/manage-users"
        }
        return modelAndView
    }
}

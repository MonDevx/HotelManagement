package com.gpch.hotel.controller

import com.gpch.hotel.model.User
import com.gpch.hotel.service.EmailSenderService
import com.gpch.hotel.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.mail.SimpleMailMessage
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class LoginController @Autowired constructor(
    private val userService: UserService,
    private val messageSource: MessageSource,
    private val emailSenderService: EmailSenderService
) {

    @RequestMapping(value = ["/", "/login"], method = [RequestMethod.GET])
    fun login(): ModelAndView {
        val modelAndView = ModelAndView()
        modelAndView.viewName = "login"
        return modelAndView
    }

    @RequestMapping(value = ["/forgot-password"], method = [RequestMethod.GET])
    fun forgotPassword(): ModelAndView {
        val modelAndView = ModelAndView()
        modelAndView.addObject("user", User())
        modelAndView.viewName = "forgot-password"
        return modelAndView
    }

    @RequestMapping(value = ["/forgot-password"], method = [RequestMethod.POST])
    fun forgotUserPassword(
        modelAndView: ModelAndView,
        user: User,
        redirectAttributes: RedirectAttributes
    ): ModelAndView {
        val existingUser = userService.findUserByEmail(user.email!!)
        modelAndView.addObject("user", User())
        if (existingUser != null) {
            val mailMessage = SimpleMailMessage()
            mailMessage.setTo(existingUser.email)
            mailMessage.setSubject("Complete Password Reset!")
            mailMessage.setText(
                "To complete the password reset process, please click here (Can Only one time click):" +
                        "http://www.hotelms.space/confirm-reset?token=" + userService.createToken(existingUser)
            )
            emailSenderService.sendEmail(mailMessage)
            redirectAttributes.addFlashAttribute(
                "message",
                messageSource.getMessage("fogot.message.text", null, LocaleContextHolder.getLocale())
            )
        } else {
            redirectAttributes.addFlashAttribute(
                "message_error",
                messageSource.getMessage("fogot.message_error.text", null, LocaleContextHolder.getLocale())
            )
        }
        modelAndView.viewName = "redirect:/forgot-password"
        return modelAndView
    }

    @RequestMapping(value = ["/confirm-reset"], method = [RequestMethod.GET, RequestMethod.POST])
    fun validateResetToken(
        modelAndView: ModelAndView,
        @RequestParam("token") confirmationToken: String,
        redirectAttributes: RedirectAttributes
    ): ModelAndView {
        val token = userService.findConfirmationToken(confirmationToken)
        if (token != null) {
            val user = userService.findUserByEmail(token.user!!.email!!)!!
            userService.deleteToken(confirmationToken)
            modelAndView.addObject("user", user)
            modelAndView.addObject("email", user.email)
            modelAndView.viewName = "resetPassword"
        } else {
            redirectAttributes.addFlashAttribute(
                "message_error",
                messageSource.getMessage("reset.message_error.text", null, LocaleContextHolder.getLocale())
            )
            modelAndView.viewName = "redirect:/forgot-password"
        }
        return modelAndView
    }

    @RequestMapping(value = ["/reset-password"], method = [RequestMethod.POST])
    fun resetUserPassword(
        modelAndView: ModelAndView,
        user: User,
        redirectAttributes: RedirectAttributes
    ): ModelAndView {
        if (user.email != null) {
            userService.forgetPassword(user.email!!, user.password!!)
            redirectAttributes.addFlashAttribute(
                "message",
                messageSource.getMessage("reset.message.text", null, LocaleContextHolder.getLocale())
            )
        } else {
            redirectAttributes.addFlashAttribute(
                "message_error",
                messageSource.getMessage("reset.message_error.text", null, LocaleContextHolder.getLocale())
            )
        }
        modelAndView.viewName = "redirect:/forgot-password"
        return modelAndView
    }
}

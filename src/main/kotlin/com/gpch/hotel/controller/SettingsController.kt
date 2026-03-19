package com.gpch.hotel.controller

import com.gpch.hotel.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class SettingsController @Autowired constructor(
    private val userService: UserService,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val messageSource: MessageSource,
    private val javaMailSender: JavaMailSender
) {

    @RequestMapping(value = ["settings/account"], method = [RequestMethod.GET])
    fun profile(): ModelAndView {
        val modelAndView = ModelAndView()
        val auth = SecurityContextHolder.getContext().authentication
        val userAccount = userService.findUserByEmail(auth.name)!!
        modelAndView.addObject("fn", userAccount.name)
        modelAndView.addObject("ln", userAccount.lastName)
        modelAndView.viewName = "settings/account"
        return modelAndView
    }

    @RequestMapping(value = ["settings/changepassword"], method = [RequestMethod.GET])
    fun changePassword(): ModelAndView {
        val modelAndView = ModelAndView()
        modelAndView.viewName = "settings/changepassword"
        return modelAndView
    }

    @RequestMapping(value = ["settings/report_problem"], method = [RequestMethod.GET])
    fun reportProblem(): ModelAndView {
        val modelAndView = ModelAndView()
        modelAndView.viewName = "settings/report_problem"
        return modelAndView
    }

    @RequestMapping(value = ["settings/sendreportproblem"], method = [RequestMethod.POST])
    fun sendReportProblem(
        redirectAttributes: RedirectAttributes,
        @RequestParam requestParams: Map<String, String>
    ): ModelAndView {
        val title = requestParams["title"]
        val email = requestParams["email"]
        val description = requestParams["description"]
        val modelAndView = ModelAndView()
        val msg = SimpleMailMessage()
        msg.setTo(email ?: "")
        msg.setSubject(title ?: "")
        msg.setText("Email : $email\n Problem details \n $description")
        javaMailSender.send(msg)
        redirectAttributes.addFlashAttribute(
            "message",
            messageSource.getMessage("report.email.send.success.text", null, LocaleContextHolder.getLocale())
        )
        modelAndView.viewName = "redirect:/settings/report_problem"
        return modelAndView
    }

    @RequestMapping(value = ["settings/updateaccount"], method = [RequestMethod.POST])
    fun updateAccount(
        redirectAttributes: RedirectAttributes,
        @RequestParam requestParams: Map<String, String>
    ): ModelAndView {
        val modelAndView = ModelAndView()
        val auth = SecurityContextHolder.getContext().authentication
        val userAccount = userService.findUserByEmail(auth.name)!!
        userAccount.name = requestParams["fn"]
        userAccount.lastName = requestParams["ln"]
        userService.updateAccount(userAccount)
        redirectAttributes.addFlashAttribute(
            "message",
            messageSource.getMessage("account.u.s.text", null, LocaleContextHolder.getLocale())
        )
        modelAndView.viewName = "redirect:/settings/account"
        return modelAndView
    }

    @RequestMapping(value = ["settings/updatepassword"], method = [RequestMethod.POST])
    fun updatePassword(
        redirectAttributes: RedirectAttributes,
        @RequestParam requestParams: Map<String, String>
    ): ModelAndView {
        val newPassword = requestParams["newp"]
        val confirmNewPassword = requestParams["cnewp"]
        val lastPassword = requestParams["lastp"]
        val modelAndView = ModelAndView()
        val auth = SecurityContextHolder.getContext().authentication
        val userAccount = userService.findUserByEmail(auth.name)!!
        if (newPassword != confirmNewPassword) {
            redirectAttributes.addFlashAttribute(
                "message",
                messageSource.getMessage("account.e.e.text", null, LocaleContextHolder.getLocale())
            )
        } else {
            if (!bCryptPasswordEncoder.matches(lastPassword, userAccount.password)) {
                redirectAttributes.addFlashAttribute(
                    "message",
                    messageSource.getMessage("account.o.e.text", null, LocaleContextHolder.getLocale())
                )
            } else {
                userService.changePassword(userAccount.id!!, newPassword!!)
                redirectAttributes.addFlashAttribute(
                    "message",
                    messageSource.getMessage("account.p.s.text", null, LocaleContextHolder.getLocale())
                )
            }
        }
        modelAndView.viewName = "redirect:/settings/changepassword"
        return modelAndView
    }
}

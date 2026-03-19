package com.gpch.hotel.controller

import com.gpch.hotel.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView

@Controller
class ProfileController @Autowired constructor(
    private val userService: UserService
) {

    @RequestMapping(value = ["profile"], method = [RequestMethod.GET])
    fun profile(): ModelAndView {
        val modelAndView = ModelAndView()
        val auth = SecurityContextHolder.getContext().authentication
        val userAccount = userService.findUserByEmail(auth.name)!!
        modelAndView.addObject("fn", userAccount.name)
        modelAndView.addObject("ln", userAccount.lastName)
        modelAndView.addObject("em", userAccount.email)
        modelAndView.viewName = "profile"
        return modelAndView
    }
}

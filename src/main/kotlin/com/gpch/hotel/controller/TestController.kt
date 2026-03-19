package com.gpch.hotel.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView

@Controller
class TestController {

    @RequestMapping(value = ["/loaderio-70f45f7671f7078d85fe8fbdfaad27ed.txt"], method = [RequestMethod.GET])
    fun testToken(): ModelAndView {
        val modelAndView = ModelAndView()
        modelAndView.viewName = "loaderio-70f45f7671f7078d85fe8fbdfaad27ed.txt"
        return modelAndView
    }
}

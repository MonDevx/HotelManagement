package com.gpch.hotel.controller

import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView
import javax.servlet.RequestDispatcher
import javax.servlet.http.HttpServletRequest

@Controller
class MyErrorController : ErrorController {

    companion object {
        private const val PATH = "/error"
    }

    @RequestMapping(value = [PATH])
    fun handleError(request: HttpServletRequest): ModelAndView {
        val status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)
        val modelAndView = ModelAndView()
        if (status != null) {
            val statusCode = status.toString().toInt()
            when (statusCode) {
                HttpStatus.NOT_FOUND.value() -> modelAndView.viewName = "error-404"
                HttpStatus.INTERNAL_SERVER_ERROR.value() -> modelAndView.viewName = "error-505"
                500 -> modelAndView.viewName = "error-500"
            }
        }
        return modelAndView
    }

    override fun getErrorPath(): String = PATH

    @RequestMapping(value = ["/access-denied"], method = [RequestMethod.GET])
    fun home(): ModelAndView {
        val modelAndView = ModelAndView()
        modelAndView.viewName = "access-denied"
        return modelAndView
    }
}

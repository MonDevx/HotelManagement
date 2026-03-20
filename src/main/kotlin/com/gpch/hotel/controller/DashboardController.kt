package com.gpch.hotel.controller

import com.gpch.hotel.service.DashboardService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView
import java.util.LinkedHashMap

@Controller
class DashboardController @Autowired constructor(
    private val dashboardService: DashboardService
) {

    @RequestMapping(value = ["dashboard"], method = [RequestMethod.GET])
    fun home(): ModelAndView {
        val surveyMap = LinkedHashMap<String, Long>()
        val modelAndView = ModelAndView()
        modelAndView.addObject("countemployees", dashboardService.countEmployees())
        modelAndView.addObject("countstores", dashboardService.countStores())
        modelAndView.addObject("sumsalary", dashboardService.sumSalary())
        modelAndView.addObject("countmaintenance", dashboardService.countMaintenance())
        modelAndView.addObject("countroomtypes", dashboardService.countRoomTypes())
        modelAndView.addObject("countrooms", dashboardService.countRooms())
        modelAndView.addObject("countavailablerooms", dashboardService.countAvailableRooms())
        modelAndView.addObject("countactivestays", dashboardService.countActiveStays())
        modelAndView.addObject("occupancyrate", dashboardService.occupancyRate())
        modelAndView.addObject("bookingstoday", dashboardService.countBookingsToday())
        modelAndView.addObject("bookingsthismonth", dashboardService.countBookingsThisMonth())
        modelAndView.addObject("bookingsthisyear", dashboardService.countBookingsThisYear())
        modelAndView.addObject("revenuetoday", dashboardService.revenueToday())
        modelAndView.addObject("revenuethismonth", dashboardService.revenueThisMonth())
        modelAndView.addObject("revenuethisyear", dashboardService.revenueThisYear())
        for (temp in dashboardService.findAllPosition()) {
            surveyMap[temp.position_name ?: ""] = dashboardService.countPosition(temp)
        }
        modelAndView.addObject("position", surveyMap)
        modelAndView.viewName = "dashboard"
        return modelAndView
    }
}

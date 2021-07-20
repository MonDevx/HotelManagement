package com.gpch.hotel.controller;

import com.gpch.hotel.model.Position;
import com.gpch.hotel.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class DashboardController {

    private DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @RequestMapping(value = "dashboard", method = RequestMethod.GET)
    public ModelAndView home() {
        Map<String, Long> surveyMap = new LinkedHashMap<>();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("countemployees", dashboardService.countEmployees());
        modelAndView.addObject("countstores", dashboardService.countStores());
        modelAndView.addObject("sumsalary", dashboardService.sumsalary());
        modelAndView.addObject("countmaintenance", dashboardService.countmaintenance());
        for (Position temp : dashboardService.findallposition()) {
            surveyMap.put(temp.getPosition_name(), dashboardService.countposition(temp));
        }
        modelAndView.addObject("position", surveyMap);
        modelAndView.setViewName("dashboard");
        return modelAndView;
    }

}

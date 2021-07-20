package com.gpch.hotel.controller;

import com.gpch.hotel.model.User;
import com.gpch.hotel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProfileController {
    private UserService userService;

    @Autowired
    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "profile", method = RequestMethod.GET)
    public ModelAndView Profile() {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User useraccount = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("fn", useraccount.getName());
        modelAndView.addObject("ln", useraccount.getLastName());
        modelAndView.addObject("em", useraccount.getEmail());
        modelAndView.setViewName("profile");
        return modelAndView;
    }

}

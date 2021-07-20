package com.gpch.hotel.controller;

import com.gpch.hotel.model.User;
import com.gpch.hotel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;
import java.util.Map;

@Controller
public class SettingsController {
    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private MessageSource messageSource;
    private JavaMailSender javaMailSender;

    @Autowired
    public SettingsController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder
            , MessageSource messageSource, JavaMailSender javaMailSender) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.messageSource = messageSource;
        this.javaMailSender = javaMailSender;
    }

    @RequestMapping(value = "settings/account", method = RequestMethod.GET)
    public ModelAndView Profile() {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User useraccount = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("fn", useraccount.getName());
        modelAndView.addObject("ln", useraccount.getLastName());
        modelAndView.setViewName("settings/account");
        return modelAndView;
    }

    @RequestMapping(value = "settings/changepassword", method = RequestMethod.GET)
    public ModelAndView changepassword() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("settings/changepassword");
        return modelAndView;
    }

    @RequestMapping(value = "settings/report_problem", method = RequestMethod.GET)
    public ModelAndView reportproblem() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("settings/report_problem");
        return modelAndView;
    }

    @RequestMapping(value = "settings/sendreportproblem", method = RequestMethod.POST)
    public ModelAndView sendreportproblem(RedirectAttributes redirectAttributes, @RequestParam Map<String, String> requestParams) {
        String title = requestParams.get("title");
        String email = requestParams.get("email");
        String description = requestParams.get("description");
        ModelAndView modelAndView = new ModelAndView();
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject(title);
        msg.setText("Email : " + email + "\n Problem details \n " +
                description);
        javaMailSender.send(msg);
        redirectAttributes.addFlashAttribute("message", messageSource.getMessage("report.email.send.success.text"
                , null, LocaleContextHolder.getLocale()));
        modelAndView.setViewName("redirect:/settings/report_problem");
        return modelAndView;
    }

    @RequestMapping(value = "settings/updateaccount", method = RequestMethod.POST)
    public ModelAndView updateaccount(RedirectAttributes redirectAttributes, @RequestParam Map<String, String> requestParams) {
        ModelAndView modelAndView = new ModelAndView();
        Locale locale = LocaleContextHolder.getLocale();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User useraccount = userService.findUserByEmail(auth.getName());
        useraccount.setName(requestParams.get("fn"));
        useraccount.setLastName(requestParams.get("ln"));
        userService.Updateaccount(useraccount);
        redirectAttributes.addFlashAttribute("message", messageSource.getMessage("account.u.s.text"
                , null, LocaleContextHolder.getLocale()));
        modelAndView.setViewName("redirect:/settings/account");
        return modelAndView;
    }

    @RequestMapping(value = "settings/updatepassword", method = RequestMethod.POST)
    public ModelAndView updatepassword(RedirectAttributes redirectAttributes, @RequestParam Map<String, String> requestParams) {
        String newpassword = requestParams.get("newp");
        String cnewpassword = requestParams.get("cnewp");
        String lasrpassword = requestParams.get("lastp");
        ModelAndView modelAndView = new ModelAndView();
        Locale locale = LocaleContextHolder.getLocale();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User useraccount = userService.findUserByEmail(auth.getName());
        if (!newpassword.equals(cnewpassword)) {
            redirectAttributes.addFlashAttribute("message", messageSource.getMessage("account.e.e.text"
                    , null, LocaleContextHolder.getLocale()));
        } else {
            if (!bCryptPasswordEncoder.matches(lasrpassword, useraccount.getPassword())) {
                redirectAttributes.addFlashAttribute("message", messageSource.getMessage("account.o.e.text"
                        , null, LocaleContextHolder.getLocale()));
            } else {
                userService.Changepassword(useraccount.getId(), newpassword);
                redirectAttributes.addFlashAttribute("message", messageSource.getMessage("account.p.s.text"
                        , null, LocaleContextHolder.getLocale()));
            }
        }
        modelAndView.setViewName("redirect:/settings/changepassword");
        return modelAndView;
    }
}

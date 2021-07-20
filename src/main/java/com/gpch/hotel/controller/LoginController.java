package com.gpch.hotel.controller;

import com.gpch.hotel.model.ConfirmationToken;
import com.gpch.hotel.model.User;
import com.gpch.hotel.service.EmailSenderService;
import com.gpch.hotel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
    private UserService userService;
    private MessageSource messageSource;
    private EmailSenderService emailSenderService;

    @Autowired
    public LoginController(UserService userService, MessageSource messageSource, EmailSenderService emailSenderService) {
        this.userService = userService;
        this.messageSource = messageSource;
        this.emailSenderService = emailSenderService;
    }

    @RequestMapping(value = {"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }



    @RequestMapping(value = {"/forgot-password"}, method = RequestMethod.GET)
    public ModelAndView forgotpassword() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", new User());
        modelAndView.setViewName("forgot-password");
        return modelAndView;
    }

    @RequestMapping(value = "/forgot-password", method = RequestMethod.POST)
    public ModelAndView forgotUserPassword(ModelAndView modelAndView, User user, RedirectAttributes redirectAttributes) {
        User existingUser = userService.findUserByEmail(user.getEmail());
        modelAndView.addObject("user", new User());
        if (existingUser != null) {
            // Create the email
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(existingUser.getEmail());
            mailMessage.setSubject("Complete Password Reset!");
            mailMessage.setText("To complete the password reset process, please click here (Can Only one time click):"
                    + "http://www.hotelms.space/confirm-reset?token=" + userService.Createtoken(existingUser));
            // Send the email
            emailSenderService.sendEmail(mailMessage);
            redirectAttributes.addFlashAttribute("message", messageSource.getMessage("fogot.message.text"
                    , null, LocaleContextHolder.getLocale()));
        } else {
            redirectAttributes.addFlashAttribute("message_error", messageSource.getMessage("fogot.message_error.text"
                    , null, LocaleContextHolder.getLocale()));
        }
        modelAndView.setViewName("redirect:/forgot-password");
        return modelAndView;
    }

    @RequestMapping(value = "/confirm-reset", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView validateResetToken(ModelAndView modelAndView, @RequestParam("token") String confirmationToken, RedirectAttributes redirectAttributes) {
        ConfirmationToken token = userService.findconfirmationToken(confirmationToken);

        if (token != null) {
            User user = userService.findUserByEmail(token.getUser().getEmail());
            userService.Deletetoken(confirmationToken);
            modelAndView.addObject("user", user);
            modelAndView.addObject("email", user.getEmail());
            modelAndView.setViewName("resetPassword");
        } else {
            redirectAttributes.addFlashAttribute("message_error", messageSource.getMessage("reset.message_error.text"
                    , null, LocaleContextHolder.getLocale()));
            modelAndView.setViewName("redirect:/forgot-password");
        }

        return modelAndView;
    }

    /**
     * Receive the token from the link sent via email and display form to reset password
     */
    @RequestMapping(value = "/reset-password", method = RequestMethod.POST)
    public ModelAndView resetUserPassword(ModelAndView modelAndView, User user, RedirectAttributes redirectAttributes) {
        if (user.getEmail() != null) {
            userService.forgetpassword(user.getEmail(), user.getPassword());
            redirectAttributes.addFlashAttribute("message", messageSource.getMessage("reset.message.text"
                    , null, LocaleContextHolder.getLocale()));
        } else {
            redirectAttributes.addFlashAttribute("message_error", messageSource.getMessage("reset.message_error.text"
                    , null, LocaleContextHolder.getLocale()));
        }
        modelAndView.setViewName("redirect:/forgot-password");
        return modelAndView;
    }


}
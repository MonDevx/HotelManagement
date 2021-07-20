package com.gpch.hotel.controller;

import com.gpch.hotel.model.User;
import com.gpch.hotel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class UsersController {
    private UserService userService;
    private MessageSource messageSource;

    @Autowired
    public UsersController(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @RequestMapping(value = "users/manage-users", method = RequestMethod.GET)
    public ModelAndView User() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("list", userService.findAll());
        modelAndView.addObject("user", new User());
        modelAndView.setViewName("users/manage-users");
        return modelAndView;
    }

    @RequestMapping(value = "users/delete_user{id}", method = RequestMethod.GET)
    public String DeleteUser(@RequestParam(name = "id") String id, RedirectAttributes redirectAttributes, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("message_error", bindingResult.getAllErrors());
        } else {
            userService.DeleteUserById(id);
            redirectAttributes.addFlashAttribute("message", messageSource.getMessage("user.d.s.text"
                    , null, LocaleContextHolder.getLocale()));
        }

        return "redirect:/users/manage-users";
    }

    @RequestMapping(value = "users/edit_user{id}", method = RequestMethod.GET)
    @ResponseBody
    public User FindUser(@RequestParam(name = "id") String id) {
        return userService.FindUserById(id);
    }

    @RequestMapping(value = "users/editpassword_user", method = RequestMethod.POST)
    public ModelAndView ChangePassword(RedirectAttributes redirectAttributes, @RequestParam Map<String, String> requestParams) {
        ModelAndView modelAndView = new ModelAndView();
        String id = requestParams.get("id");
        String newpassword = requestParams.get("newp");
        String cnewpassword = requestParams.get("cnewp");
        if (!newpassword.equals(cnewpassword)) {
            redirectAttributes.addFlashAttribute("message_error", messageSource.getMessage("cp.e.text"
                    , null, LocaleContextHolder.getLocale()));
        } else {
            userService.Changepassword(id, newpassword);
            redirectAttributes.addFlashAttribute("message", messageSource.getMessage("cp.u.s.text"
                    , null, LocaleContextHolder.getLocale()));
        }

        modelAndView.setViewName("redirect:/users/manage-users");
        return modelAndView;
    }

    @RequestMapping(value = "users/updateuser{id}", method = RequestMethod.POST)
    public ModelAndView UpdateUser(@Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes, @RequestParam(name = "id") String id) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("message_error", bindingResult.getAllErrors());
        } else {
            user.setId(id);
            userService.Updateuser(user);
            redirectAttributes.addFlashAttribute("message", messageSource.getMessage("user.u.s.text"
                    , null, LocaleContextHolder.getLocale()));
        }

        modelAndView.setViewName("redirect:/users/manage-users");
        return modelAndView;
    }

    @RequestMapping(value = "users/add_user", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "public.user",
                            messageSource.getMessage("user.a.a.e.e.text"
                                    , null, LocaleContextHolder.getLocale()));
        }
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("list", userService.findAll());
            modelAndView.addObject("user", user);
            modelAndView.addObject("message_error", messageSource.getMessage("user.a.a.e.text"
                    , null, LocaleContextHolder.getLocale()));
            modelAndView.setViewName("users/manage-users");
        } else {
            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("message", messageSource.getMessage("user.a.s.text"
                    , null, LocaleContextHolder.getLocale()));
            modelAndView.setViewName("redirect:/users/manage-users");
        }
        return modelAndView;
    }
}

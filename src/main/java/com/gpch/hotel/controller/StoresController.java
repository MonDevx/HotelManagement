package com.gpch.hotel.controller;

import com.gpch.hotel.model.Store;
import com.gpch.hotel.service.StoreService;
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
import java.util.List;

@Controller
public class StoresController {
    private StoreService storeService;
    private MessageSource messageSource;

    @Autowired
    public StoresController(StoreService storeService, MessageSource messageSource) {
        this.storeService = storeService;
        this.messageSource = messageSource;
    }

    @RequestMapping(value = "stores/manage-stores", method = RequestMethod.GET)
    public ModelAndView Stores() {
        ModelAndView modelAndView = new ModelAndView();
        List<Store> liststores = storeService.findAll();
        modelAndView.addObject("store", new Store());
        modelAndView.addObject("list", liststores);
        modelAndView.setViewName("stores/manage-stores");
        return modelAndView;
    }

    @RequestMapping(value = "stores/delete_store{id}", method = RequestMethod.GET)
    public String DeleteStores(@RequestParam(name = "id") int id, RedirectAttributes redirectAttributes) {
        storeService.DeleteStoreById(id);
        redirectAttributes.addFlashAttribute("message", messageSource.getMessage("store.d.s.text"
                , null, LocaleContextHolder.getLocale()));
        return "redirect:/stores/manage-stores";
    }

    @RequestMapping(value = "stores/edit_store{id}", method = RequestMethod.GET)
    @ResponseBody
    public Store FindUser(@RequestParam(name = "id") int id) {
        return storeService.FindStoreById(id);
    }

    @RequestMapping(value = "stores/updatestore{id}", method = RequestMethod.POST)
    public ModelAndView UpdateStore(@Valid Store store, BindingResult bindingResult, RedirectAttributes redirectAttributes,@RequestParam(name = "id") int id) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("message_error", bindingResult.getAllErrors());
        } else {
            store.setId(id);
            storeService.Updatestore(store);
            redirectAttributes.addFlashAttribute("message", messageSource.getMessage("store.u.s.text"
                    , null, LocaleContextHolder.getLocale()));
        }
        modelAndView.setViewName("redirect:/stores/manage-stores");
        return modelAndView;
    }

    @RequestMapping(value = "stores/add_store", method = RequestMethod.POST)
    public ModelAndView AddStore(@Valid Store store, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("message_error", bindingResult.getAllErrors());
        } else {
            storeService.saveStore(store);
            redirectAttributes.addFlashAttribute("message", messageSource.getMessage("store.a.s.text"
                    , null, LocaleContextHolder.getLocale()));
        }
        modelAndView.setViewName("redirect:/stores/manage-stores");
        return modelAndView;
    }
}

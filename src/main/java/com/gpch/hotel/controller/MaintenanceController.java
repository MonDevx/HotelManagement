package com.gpch.hotel.controller;

import com.gpch.hotel.model.Maintenance;
import com.gpch.hotel.service.MaintenanceService;
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

@Controller
public class MaintenanceController {

    private MaintenanceService maintenanceService;
    private MessageSource messageSource;

    @Autowired
    public MaintenanceController(MaintenanceService maintenanceService, MessageSource messageSource) {
        this.maintenanceService = maintenanceService;
        this.messageSource = messageSource;
    }

    @RequestMapping(value = "maintenances/add-maintenance", method = RequestMethod.GET)
    public ModelAndView Maintenance() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("maintenance", new Maintenance());
        modelAndView.setViewName("maintenances/add-maintenance");
        return modelAndView;
    }

    @RequestMapping(value = "maintenances/manage-maintenance", method = RequestMethod.GET)
    public ModelAndView Maintenances() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("list", maintenanceService.findAll());
        modelAndView.addObject("maintenance", new Maintenance());
        modelAndView.setViewName("maintenances/manage-maintenance");
        return modelAndView;
    }

    @RequestMapping(value = "maintenances/add_maintenance", method = RequestMethod.POST)
    public ModelAndView Addmaintenance(@Valid Maintenance maintenance, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("message_error", bindingResult.getAllErrors());
        } else {
            maintenanceService.saveMaintenance(maintenance);
            redirectAttributes.addFlashAttribute("message", messageSource.getMessage("maintenance.a.s.text"
                    , null, LocaleContextHolder.getLocale()));

        }
        modelAndView.setViewName("redirect:/maintenances/add-maintenance");
        return modelAndView;
    }

    @RequestMapping(value = "maintenances/delete_maintenance{id}", method = RequestMethod.GET)
    public String Deletemaintenance(@RequestParam(name = "id") String id, RedirectAttributes redirectAttributes) {
        maintenanceService.DeleteMaintenanceById(id);
        redirectAttributes.addFlashAttribute("message", messageSource.getMessage("maintenance.d.s.text"
                , null, LocaleContextHolder.getLocale()));
        return "redirect:/maintenances/manage-maintenance";
    }

    @RequestMapping(value = "maintenances/edit_maintenance{id}", method = RequestMethod.GET)
    @ResponseBody
    public Maintenance FindMaintenance(@RequestParam(name = "id") String id) {
        return maintenanceService.FindMaintenanceById(id);
    }

    @RequestMapping(value = "maintenances/updatemaintenance{id}", method = RequestMethod.POST)
    public ModelAndView UpdateMaintenance(@Valid Maintenance maintenance, @RequestParam(name = "id") String id, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("message_error", bindingResult.getAllErrors());
        } else {
            maintenance.setId(id);
            maintenanceService.Updatemaintenance(maintenance);
            redirectAttributes.addFlashAttribute("message", messageSource.getMessage("maintenance.u.s.text"
                    , null, LocaleContextHolder.getLocale()));
        }

        modelAndView.setViewName("redirect:/maintenances/manage-maintenance");
        return modelAndView;
    }
}

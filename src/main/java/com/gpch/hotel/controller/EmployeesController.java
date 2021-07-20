package com.gpch.hotel.controller;

import com.gpch.hotel.view.ExcelEmployeeListReportView;
import com.gpch.hotel.view.PdfEmployeeListReportView;
import com.gpch.hotel.model.Employee;
import com.gpch.hotel.service.DashboardService;
import com.gpch.hotel.service.EmployeeService;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class EmployeesController {

    private EmployeeService employeeService;
    private DashboardService dashboardService;
    private MessageSource messageSource;

    @Autowired
    public EmployeesController(EmployeeService employeeService, DashboardService dashboardService, MessageSource messageSource) {
        this.employeeService = employeeService;
        this.dashboardService = dashboardService;
        this.messageSource = messageSource;
    }


    @RequestMapping(value = "employees/manage-employees", method = RequestMethod.GET)
    public ModelAndView Employees() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("list", employeeService.findAll());
        modelAndView.addObject("listposition", dashboardService.findallposition());
        modelAndView.addObject("employee", new Employee());
        modelAndView.setViewName("employees/manage-employees");
        return modelAndView;
    }

    @RequestMapping(value = "employees/delete_employee{id}", method = RequestMethod.GET)
    public String DeleteEmployee(@RequestParam(name = "id") String id, RedirectAttributes redirectAttributes) {
        employeeService.DeleteEmployeeById(id);
        redirectAttributes.addFlashAttribute("message", messageSource.getMessage("employee.d.s.text"
                , null, LocaleContextHolder.getLocale()));
        return "redirect:/employees/manage-employees";
    }

    @RequestMapping(value = "employees/edit_employee{id}", method = RequestMethod.GET)
    @ResponseBody
    public Employee FindEmployeeByid(@RequestParam(name = "id") String id) {
        return employeeService.FindEmployeeById(id);
    }


    @RequestMapping(value = "/employees/updateemployee{id}", method = RequestMethod.POST)
    public ModelAndView Updateemployee(@Valid Employee employee,@RequestParam(name = "id") String id, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("message_error", bindingResult.getAllErrors());
        } else {
            employee.setId(id);
            employeeService.Updateemployee(employee);
            redirectAttributes.addFlashAttribute("message", messageSource.getMessage("employee.u.s.text"
                    , null, LocaleContextHolder.getLocale()));
        }
        modelAndView.setViewName("redirect:/employees/manage-employees");
        return modelAndView;
    }

    @RequestMapping(value = "employees/add_employee", method = RequestMethod.POST)
    public ModelAndView Addemployee(@Valid Employee employee, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("message_error", bindingResult.getAllErrors());
        } else {
            employeeService.saveEmployee(employee);
            redirectAttributes.addFlashAttribute("message", messageSource.getMessage("employee.a.s.text"
                    , null, LocaleContextHolder.getLocale()));
        }
        modelAndView.setViewName("redirect:/employees/manage-employees");
        return modelAndView;
    }

    @RequestMapping(value = "employees/report", method = RequestMethod.GET)
    public ModelAndView EmployeeListReport(HttpServletRequest req) {

        String typeReport = req.getParameter("type");
        if (typeReport != null && typeReport.equals("xls")) {
            return new ModelAndView(new ExcelEmployeeListReportView(), "employeeList", employeeService.findAll());
        } else if (typeReport != null && typeReport.equals("pdf")) {
            return new ModelAndView(new PdfEmployeeListReportView(), "employeeList", employeeService.findAll());
        }
        return new ModelAndView("redirect:/employees/manage-employees");
    }
}

package com.gpch.hotel.controller

import com.gpch.hotel.model.Employee
import com.gpch.hotel.service.DashboardService
import com.gpch.hotel.service.EmployeeService
import com.gpch.hotel.view.ExcelEmployeeListReportView
import com.gpch.hotel.view.PdfEmployeeListReportView
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@Controller
class EmployeesController @Autowired constructor(
    private val employeeService: EmployeeService,
    private val dashboardService: DashboardService,
    private val messageSource: MessageSource
) {

    @RequestMapping(value = ["employees/manage-employees"], method = [RequestMethod.GET])
    fun employees(): ModelAndView {
        val modelAndView = ModelAndView()
        modelAndView.addObject("list", employeeService.findAll())
        modelAndView.addObject("listposition", dashboardService.findAllPosition())
        modelAndView.addObject("employee", Employee())
        modelAndView.viewName = "employees/manage-employees"
        return modelAndView
    }

    @RequestMapping(value = ["employees/delete_employee{id}"], method = [RequestMethod.GET])
    fun deleteEmployee(
        @RequestParam(name = "id") id: Long,
        redirectAttributes: RedirectAttributes
    ): String {
        employeeService.deleteEmployeeById(id)
        redirectAttributes.addFlashAttribute(
            "message",
            messageSource.getMessage("employee.d.s.text", null, LocaleContextHolder.getLocale())
        )
        return "redirect:/employees/manage-employees"
    }

    @RequestMapping(value = ["employees/edit_employee{id}"], method = [RequestMethod.GET])
    @ResponseBody
    fun findEmployeeById(@RequestParam(name = "id") id: Long): Employee? =
        employeeService.findEmployeeById(id)

    @RequestMapping(value = ["/employees/updateemployee{id}"], method = [RequestMethod.POST])
    fun updateEmployee(
        @Valid employee: Employee,
        @RequestParam(name = "id") id: Long,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes
    ): ModelAndView {
        val modelAndView = ModelAndView()
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("message_error", bindingResult.allErrors)
        } else {
            employee.id = id
            employeeService.updateEmployee(employee)
            redirectAttributes.addFlashAttribute(
                "message",
                messageSource.getMessage("employee.u.s.text", null, LocaleContextHolder.getLocale())
            )
        }
        modelAndView.viewName = "redirect:/employees/manage-employees"
        return modelAndView
    }

    @RequestMapping(value = ["employees/add_employee"], method = [RequestMethod.POST])
    fun addEmployee(
        @Valid employee: Employee,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes
    ): ModelAndView {
        val modelAndView = ModelAndView()
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("message_error", bindingResult.allErrors)
        } else {
            employeeService.saveEmployee(employee)
            redirectAttributes.addFlashAttribute(
                "message",
                messageSource.getMessage("employee.a.s.text", null, LocaleContextHolder.getLocale())
            )
        }
        modelAndView.viewName = "redirect:/employees/manage-employees"
        return modelAndView
    }

    @RequestMapping(value = ["employees/report"], method = [RequestMethod.GET])
    fun employeeListReport(req: HttpServletRequest): ModelAndView {
        val typeReport = req.getParameter("type")
        if (typeReport != null && typeReport == "xls") {
            return ModelAndView(ExcelEmployeeListReportView(), "employeeList", employeeService.findAll())
        } else if (typeReport != null && typeReport == "pdf") {
            return ModelAndView(PdfEmployeeListReportView(), "employeeList", employeeService.findAll())
        }
        return ModelAndView("redirect:/employees/manage-employees")
    }
}

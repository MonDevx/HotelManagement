package com.gpch.hotel.view

import com.gpch.hotel.model.Employee
import org.apache.poi.ss.usermodel.Workbook
import org.springframework.web.servlet.view.document.AbstractXlsView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ExcelEmployeeListReportView : AbstractXlsView() {

    @Suppress("UNCHECKED_CAST")
    override fun buildExcelDocument(
        model: Map<String, Any>,
        workbook: Workbook,
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        response.setHeader("Content-disposition", "attachment; filename=\"employee_list.xls\"")

        val list = model["employeeList"] as List<Employee>

        val sheet = workbook.createSheet("Employee List")

        val header = sheet.createRow(0)
        header.createCell(0).setCellValue("FIRST NAME")
        header.createCell(1).setCellValue("LAST NAME")
        header.createCell(2).setCellValue("SALARY")
        header.createCell(3).setCellValue("POSITION")

        var rowNum = 1
        for (employee in list) {
            val row = sheet.createRow(rowNum++)
            row.createCell(0).setCellValue(employee.firstName)
            row.createCell(1).setCellValue(employee.lastName)
            row.createCell(2).setCellValue(employee.salary.toDouble())
            row.createCell(3).setCellValue(employee.positions?.position_name)
        }
    }
}

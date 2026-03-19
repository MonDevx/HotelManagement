package com.gpch.hotel.view

import com.gpch.hotel.model.Employee
import com.lowagie.text.Document
import com.lowagie.text.Table
import com.lowagie.text.pdf.PdfWriter
import org.springframework.web.servlet.view.document.AbstractPdfView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class PdfEmployeeListReportView : AbstractPdfView() {

    @Suppress("UNCHECKED_CAST")
    override fun buildPdfDocument(
        model: Map<String, Any>,
        document: Document,
        writer: PdfWriter,
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        response.setHeader("Content-Disposition", "attachment; filename=\"Employee_list.pdf\"")

        val list = model["employeeList"] as List<Employee>

        val table = Table(4)

        table.addCell("FIRST NAME")
        table.addCell("LAST NAME")
        table.addCell("SALARY")
        table.addCell("POSITION")

        for (employee in list) {
            table.addCell(employee.firstName)
            table.addCell(employee.lastName)
            table.addCell(employee.salary.toString())
            table.addCell(employee.positions?.position_name)
        }

        document.add(table)
    }
}

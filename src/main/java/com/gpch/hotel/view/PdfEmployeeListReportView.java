package com.gpch.hotel.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.gpch.hotel.model.Employee;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Document;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;

public class PdfEmployeeListReportView extends AbstractPdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {

        response.setHeader("Content-Disposition", "attachment; filename=\"Employee_list.pdf\"");

        @SuppressWarnings("unchecked")
        List<Employee> list = (List<Employee>) model.get("employeeList");

        Table table = new Table(4);

        table.addCell("FIRST NAME");
        table.addCell("LAST NAME");
        table.addCell("SALARY");
        table.addCell("POSITION");

        for(Employee employee: list){
            table.addCell(employee.getFirstName());
            table.addCell(employee.getLastName());
            table.addCell(String.valueOf(employee.getSalary()));
            table.addCell(employee.getPositions().getPosition_name());
        }

        document.add(table);
    }

}
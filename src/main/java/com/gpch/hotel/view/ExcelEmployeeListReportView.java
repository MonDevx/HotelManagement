package com.gpch.hotel.view;


import com.gpch.hotel.model.Employee;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;




public class ExcelEmployeeListReportView extends AbstractXlsView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
                                      HttpServletResponse response) {

        response.setHeader("Content-disposition", "attachment; filename=\"employee_list.xls\"");

        @SuppressWarnings("unchecked")
        List<Employee> list = (List<Employee>) model.get("employeeList");

        Sheet sheet = workbook.createSheet("Employee List");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("FIRST NAME");
        header.createCell(1).setCellValue("LAST NAME");
        header.createCell(2).setCellValue("SALARY");
        header.createCell(3).setCellValue("POSITION");

        int rowNum = 1;

        for(Employee employee : list){
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(employee.getFirstName());
            row.createCell(1).setCellValue(employee.getLastName());
            row.createCell(2).setCellValue(employee.getSalary());
            row.createCell(3).setCellValue(employee.getPositions().getPosition_name());

        }

    }

}
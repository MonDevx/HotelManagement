package com.gpch.hotel.service;

import com.gpch.hotel.model.Employee;
import com.gpch.hotel.repository.EmployeeRepository;
import com.gpch.hotel.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("employeeService")
@Transactional
public class EmployeeService {
    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(@Qualifier("employeeRepository") EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public void DeleteEmployeeById(String id) {
        employeeRepository.deleteById(id);
    }

    public Employee FindEmployeeById(String id) {
        return employeeRepository.findById(id);
    }

    public void Updateemployee(Employee employee) {
        Employee employeeFromDb = employeeRepository.findById(employee.getId());
        employeeFromDb.setFirstName(employee.getFirstName());
        employeeFromDb.setLastName(employee.getLastName());
        employeeFromDb.setSalary(employee.getSalary());
        employeeFromDb.setPositions(employee.getPositions());
        employeeFromDb.setPhone(employee.getPhone());
        employeeFromDb.setAddress(employee.getAddress());
        employeeRepository.save(employeeFromDb);
    }
    public void saveEmployee(Employee employee) {
        employeeRepository.save(employee);
    }
}

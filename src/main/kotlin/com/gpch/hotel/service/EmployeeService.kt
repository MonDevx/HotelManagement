package com.gpch.hotel.service

import com.gpch.hotel.model.Employee
import com.gpch.hotel.repository.EmployeeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service("employeeService")
@Transactional
class EmployeeService @Autowired constructor(
    @Qualifier("employeeRepository") private val employeeRepository: EmployeeRepository
) {

    fun findAll(): List<Employee> = employeeRepository.findAll()

    fun deleteEmployeeById(id: Long) = employeeRepository.deleteById(id)

    fun findEmployeeById(id: Long): Employee? = employeeRepository.findById(id).orElse(null)

    fun updateEmployee(employee: Employee) {
        val employeeFromDb = employeeRepository.findById(employee.id).orElse(null)!!
        employeeFromDb.firstName = employee.firstName
        employeeFromDb.lastName = employee.lastName
        employeeFromDb.salary = employee.salary
        employeeFromDb.positions = employee.positions
        employeeFromDb.phone = employee.phone
        employeeFromDb.address = employee.address
        employeeRepository.save(employeeFromDb)
    }

    fun saveEmployee(employee: Employee) = employeeRepository.save(employee)
}

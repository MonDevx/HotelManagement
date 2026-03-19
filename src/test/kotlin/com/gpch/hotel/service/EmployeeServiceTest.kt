package com.gpch.hotel.service

import com.gpch.hotel.model.Employee
import com.gpch.hotel.model.Position
import com.gpch.hotel.repository.EmployeeRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import java.util.Optional

class EmployeeServiceTest {

    private val employeeRepository = mock(EmployeeRepository::class.java)
    private val employeeService = EmployeeService(employeeRepository)

    @Test
    fun `find all returns repository results`() {
        val employees = listOf(employee(id = 1L), employee(id = 2L))
        `when`(employeeRepository.findAll()).thenReturn(employees)

        val result = employeeService.findAll()

        assertEquals(employees, result)
        verify(employeeRepository).findAll()
    }

    @Test
    fun `find by id returns null when employee does not exist`() {
        `when`(employeeRepository.findById(55L)).thenReturn(Optional.empty())

        val result = employeeService.findEmployeeById(55L)

        assertNull(result)
        verify(employeeRepository).findById(55L)
    }

    @Test
    fun `update employee copies mutable fields before saving`() {
        val position = position(1, "Receptionist")
        val existingEmployee = employee(
            id = 4L,
            firstName = "Jane",
            lastName = "Doe",
            salary = 2000,
            position = position,
            phone = "1234",
            address = "Old Street"
        )
        val updatedEmployee = employee(
            id = 4L,
            firstName = "John",
            lastName = "Smith",
            salary = 2500,
            position = position(2, "Manager"),
            phone = "5678",
            address = "New Street"
        )
        `when`(employeeRepository.findById(4L)).thenReturn(Optional.of(existingEmployee))

        employeeService.updateEmployee(updatedEmployee)

        assertEquals(updatedEmployee.firstName, existingEmployee.firstName)
        assertEquals(updatedEmployee.lastName, existingEmployee.lastName)
        assertEquals(updatedEmployee.salary, existingEmployee.salary)
        assertEquals(updatedEmployee.positions, existingEmployee.positions)
        assertEquals(updatedEmployee.phone, existingEmployee.phone)
        assertEquals(updatedEmployee.address, existingEmployee.address)
        verify(employeeRepository).save(existingEmployee)
    }

    private fun employee(
        id: Long,
        firstName: String = "First",
        lastName: String = "Last",
        salary: Int = 0,
        position: Position? = null,
        phone: String? = null,
        address: String? = null
    ): Employee {
        val employee = Employee()
        employee.id = id
        employee.firstName = firstName
        employee.lastName = lastName
        employee.salary = salary
        employee.positions = position
        employee.phone = phone
        employee.address = address
        return employee
    }

    private fun position(id: Int, name: String): Position {
        val position = Position()
        position.id = id
        position.position_name = name
        return position
    }
}

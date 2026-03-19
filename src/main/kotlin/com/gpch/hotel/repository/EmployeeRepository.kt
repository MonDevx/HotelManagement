package com.gpch.hotel.repository

import com.gpch.hotel.model.Employee
import com.gpch.hotel.model.Position
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository("employeeRepository")
interface EmployeeRepository : JpaRepository<Employee, Long> {

    fun countByPositions(position: Position): Long

    @Query("SELECT SUM(e.salary) FROM Employee e")
    fun salaryTotals(): Int?
}

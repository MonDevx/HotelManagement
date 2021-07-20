package com.gpch.hotel.repository;

import com.gpch.hotel.model.Employee;
import com.gpch.hotel.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("employeeRepository")
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Employee findById(String id);

    Long countByPositions(Position position);

    @Query("SELECT SUM(e.salary) FROM Employee e")
    Integer salarytTotals();

    void deleteById(String id);
}

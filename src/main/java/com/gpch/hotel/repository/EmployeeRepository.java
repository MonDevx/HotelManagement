package com.gpch.hotel.repository;

import com.gpch.hotel.model.Employee;
import com.gpch.hotel.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("employeeRepository")
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findById(long id);

    Long countByPositions(Position position);

    @Query("SELECT SUM(e.salary) FROM Employee e")
    Integer salarytTotals();

    void deleteById(long id);
}

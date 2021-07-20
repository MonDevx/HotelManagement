package com.gpch.hotel.repository;

import com.gpch.hotel.model.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("maintenanceRepository")
public interface MaintenanceRepository extends JpaRepository<Maintenance, Integer> {
    Maintenance findById(String id);

    long countByStatus(String status);

    void deleteById(String id);
}

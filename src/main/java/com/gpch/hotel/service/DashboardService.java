package com.gpch.hotel.service;

import com.gpch.hotel.model.Position;
import com.gpch.hotel.repository.EmployeeRepository;
import com.gpch.hotel.repository.MaintenanceRepository;
import com.gpch.hotel.repository.PositionRepository;
import com.gpch.hotel.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("dashboardService")
@Transactional
public class DashboardService {
    private MaintenanceRepository maintenanceRepository;
    private EmployeeRepository employeeRepository;
    private StoreRepository storeRepository;
    private PositionRepository positionRepository;

    @Autowired
    public DashboardService(MaintenanceRepository maintenanceRepository, EmployeeRepository employeeRepository, StoreRepository storeRepository, PositionRepository positionRepository) {
        this.maintenanceRepository = maintenanceRepository;
        this.employeeRepository = employeeRepository;
        this.storeRepository = storeRepository;
        this.positionRepository = positionRepository;
    }


    public long countEmployees() {
        return employeeRepository.count();
    }

    public long countposition(Position position) {
        return employeeRepository.countByPositions(position);
    }

    public long countStores() {
        return storeRepository.count();
    }

    public Integer sumsalary() {
        return employeeRepository.salarytTotals();
    }

    public long countmaintenance() {
        return maintenanceRepository.countByStatus("Not_yet_implemented");
    }

    public List<Position> findallposition() {
        return positionRepository.findAll();
    }
}

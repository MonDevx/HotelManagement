package com.gpch.hotel.service;

import com.gpch.hotel.model.Maintenance;
import com.gpch.hotel.repository.MaintenanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

@Service("maintenanceService")
@Transactional
public class MaintenanceService {
    private MaintenanceRepository maintenanceRepository;

    @Autowired
    public MaintenanceService(@Qualifier("maintenanceRepository") MaintenanceRepository maintenanceRepository) {
        this.maintenanceRepository = maintenanceRepository;
    }

    public void saveMaintenance(Maintenance maintenance) {
        Calendar currenttime = Calendar.getInstance();
        maintenance.setDate(new Date((currenttime.getTime()).getTime()));
        maintenance.setStatus("Not_yet_implemented");
        maintenanceRepository.save(maintenance);
    }

    public List<Maintenance> findAll() {
        return maintenanceRepository.findAll();
    }

    public void DeleteMaintenanceById(String id) {
        maintenanceRepository.deleteById(id);
    }

    public Maintenance FindMaintenanceById(String id) {
        return maintenanceRepository.findById(id);
    }

    public void Updatemaintenance(Maintenance maintenance) {
        Maintenance storeFromDb = maintenanceRepository.findById(maintenance.getId());
        storeFromDb.setStatus(maintenance.getStatus());
        maintenanceRepository.save(storeFromDb);
    }
}

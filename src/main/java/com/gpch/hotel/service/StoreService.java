package com.gpch.hotel.service;

import com.gpch.hotel.model.Store;
import com.gpch.hotel.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("storeService")
@Transactional
public class StoreService {
    private StoreRepository storeRepository;

    @Autowired
    public StoreService(@Qualifier("storeRepository") StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public List<Store> findAll() {
        return storeRepository.findAll();
    }

    public void DeleteStoreById(int id) {
        storeRepository.deleteById(id);
    }

    public Store FindStoreById(int id) {
        return storeRepository.findById(id);
    }

    public void Updatestore(Store store) {
        Store storeFromDb = storeRepository.findById(store.getId());
        storeFromDb.setStoreName(store.getStoreName());
        storeFromDb.setStatus(store.getStatus());
        storeRepository.save(storeFromDb);
    }

    public void saveStore(Store store) {

        storeRepository.save(store);
    }
}

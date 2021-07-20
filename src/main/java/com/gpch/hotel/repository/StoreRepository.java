package com.gpch.hotel.repository;

import com.gpch.hotel.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository("storeRepository")
public interface StoreRepository extends JpaRepository<Store, Integer> {
    Store findById(int id);
}

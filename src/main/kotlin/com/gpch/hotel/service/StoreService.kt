package com.gpch.hotel.service

import com.gpch.hotel.model.Store
import com.gpch.hotel.repository.StoreRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service("storeService")
@Transactional
class StoreService @Autowired constructor(
    @Qualifier("storeRepository") private val storeRepository: StoreRepository
) {

    fun findAll(): List<Store> = storeRepository.findAll()

    fun deleteStoreById(id: Long) = storeRepository.deleteById(id)

    fun findStoreById(id: Long): Store? = storeRepository.findById(id).orElse(null)

    fun updateStore(store: Store) {
        val storeFromDb = storeRepository.findById(store.id).orElse(null)!!
        storeFromDb.storeName = store.storeName
        storeFromDb.status = store.status
        storeRepository.save(storeFromDb)
    }

    fun saveStore(store: Store) = storeRepository.save(store)
}

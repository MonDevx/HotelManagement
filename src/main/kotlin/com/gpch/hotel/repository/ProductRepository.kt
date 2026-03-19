package com.gpch.hotel.repository

import com.gpch.hotel.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository("productRepository")
interface ProductRepository : JpaRepository<Product, Long>

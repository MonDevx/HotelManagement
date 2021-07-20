package com.gpch.hotel.repository;

import com.gpch.hotel.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository("productRepository")
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findById(String id);

    void deleteById(String id);
}

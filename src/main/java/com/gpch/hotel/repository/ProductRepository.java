package com.gpch.hotel.repository;

import com.gpch.hotel.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository("productRepository")
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findById(long id);

    void deleteById(long id);
}

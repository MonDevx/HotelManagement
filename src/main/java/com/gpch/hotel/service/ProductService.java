package com.gpch.hotel.service;

import com.gpch.hotel.model.Product;
import com.gpch.hotel.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("productService")
@Transactional
public class ProductService {
    private ProductRepository productRepository;

    @Autowired
    public ProductService(@Qualifier("productRepository") ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public void DeleteProductById(String id) {
        productRepository.deleteById(id);
    }

    public Product FindProductById(String id) {
        return productRepository.findById(id);
    }

    public void Updateproduct(Product product) {
        Product productFromDb = productRepository.findById(product.getId());
        productFromDb.setStores(product.getStores());
        productFromDb.setProductname(product.getProductname());
        productFromDb.setPrice(product.getPrice());
        productRepository.save(productFromDb);
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }
}

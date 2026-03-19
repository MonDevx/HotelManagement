package com.gpch.hotel.service

import com.gpch.hotel.model.Product
import com.gpch.hotel.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service("productService")
@Transactional
class ProductService @Autowired constructor(
    @Qualifier("productRepository") private val productRepository: ProductRepository
) {

    fun findAll(): List<Product> = productRepository.findAll()

    fun deleteProductById(id: Long) = productRepository.deleteById(id)

    fun findProductById(id: Long): Product? = productRepository.findById(id).orElse(null)

    fun updateProduct(product: Product) {
        val productFromDb = productRepository.findById(product.id).orElse(null)!!
        productFromDb.stores = product.stores
        productFromDb.productName = product.productName
        productFromDb.price = product.price
        productRepository.save(productFromDb)
    }

    fun saveProduct(product: Product) = productRepository.save(product)
}

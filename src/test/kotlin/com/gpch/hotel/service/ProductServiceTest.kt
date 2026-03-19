package com.gpch.hotel.service

import com.gpch.hotel.model.Product
import com.gpch.hotel.model.Store
import com.gpch.hotel.repository.ProductRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import java.util.Optional

class ProductServiceTest {

    private val productRepository = mock(ProductRepository::class.java)
    private val productService = ProductService(productRepository)

    @Test
    fun `find all returns repository results`() {
        val products = listOf(product(id = 1L), product(id = 2L))
        `when`(productRepository.findAll()).thenReturn(products)

        val result = productService.findAll()

        assertEquals(products, result)
        verify(productRepository).findAll()
    }

    @Test
    fun `find by id returns null when product does not exist`() {
        `when`(productRepository.findById(99L)).thenReturn(Optional.empty())

        val result = productService.findProductById(99L)

        assertNull(result)
        verify(productRepository).findById(99L)
    }

    @Test
    fun `update product copies mutable fields before saving`() {
        val store = store(1L, "Lobby")
        val existingProduct = product(id = 7L, name = "Water", price = 10, store = store)
        val updatedProduct = product(id = 7L, name = "Juice", price = 20, store = store(2L, "Kitchen"))
        `when`(productRepository.findById(7L)).thenReturn(Optional.of(existingProduct))

        productService.updateProduct(updatedProduct)

        assertEquals(updatedProduct.productName, existingProduct.productName)
        assertEquals(updatedProduct.price, existingProduct.price)
        assertEquals(updatedProduct.stores, existingProduct.stores)
        verify(productRepository).save(existingProduct)
    }

    private fun product(id: Long, name: String = "Product", price: Int = 0, store: Store? = null): Product {
        val product = Product()
        product.id = id
        product.productName = name
        product.price = price
        product.stores = store
        return product
    }

    private fun store(id: Long, name: String): Store {
        val store = Store()
        store.id = id
        store.storeName = name
        return store
    }
}

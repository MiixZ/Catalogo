package htt.catalogo.api

import htt.catalogo.model.Product

class ApiTest {
    suspend fun getAllProducts(): List<Product> {
        return listOf(
            Product(1, "Producto 1", 10.0),
            Product(2, "Producto 2", 15.5),
            Product(3, "Producto 3", 20.0)
        )
    }
}
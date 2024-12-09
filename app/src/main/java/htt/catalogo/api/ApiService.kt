package htt.catalogo.api

import htt.catalogo.model.Product
import retrofit2.http.GET

interface ApiService {
    @GET("product/getAllProducts")
    suspend fun getAllProducts(): List<Product>
}
package htt.catalogo

import htt.catalogo.models.Product
import retrofit2.http.GET

interface ApiService {
    @GET("product/getAllProducts")
    suspend fun getAllProducts(): List<Product>
}
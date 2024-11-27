package htt.catalogo

import retrofit2.http.GET

interface ApiService {
    @GET("product/getAllProducts")
    suspend fun getAllProducts(): List<Product>
}
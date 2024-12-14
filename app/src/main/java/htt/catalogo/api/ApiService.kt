package htt.catalogo.api

import htt.catalogo.model.Product
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("product/getAllProducts")
    suspend fun getAllProducts(): List<Product>

    @GET("cart/addProductToCart")
    suspend fun addProductToCart(@Query("email") email: String, @Query("productId") id: String)

    @GET("cart/getCartProducts")
    suspend fun getCartProducts(@Query("email") email: String): List<Product>

    @GET("cart/removeProductFromCart")
    suspend fun removeProductFromCart(id: String): List<Product>
}
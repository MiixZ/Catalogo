package htt.catalogo.api

import htt.catalogo.model.Product
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("product/getAllProducts")
    suspend fun getAllProducts(): List<Product>

    @GET("cart/addProductToCart/{email}/{id}")
    suspend fun addProductToCart(@Path("email") email: String, @Path("id") id: String)

    @GET("cart/getCartProducts")
    suspend fun getCartProducts(): List<Product>

    @GET("cart/removeProductFromCart")
    suspend fun removeProductFromCart(id: String): List<Product>
}
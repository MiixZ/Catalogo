package htt.catalogo.api

import htt.catalogo.model.Product
import htt.catalogo.model.User
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("product/getAllProducts")
    suspend fun getAllProducts(): List<Product>

    @GET("cart/addProductToCart")
    suspend fun addProductToCart(@Query("email") email: String, @Query("productId") id: String)

    @GET("cart/getCartProducts")
    suspend fun getCartProducts(@Query("email") email: String): List<Product>

    @GET("cart/removeProductFromCart")
    suspend fun removeProductFromCart(@Query("email") email: String, @Query("productId") id: String)

    @GET("user/login")
    suspend fun login(@Query("email") email: String, @Query("password") password: String): User

    @POST("product/createProduct")
    suspend fun addProduct(@Body product: Product): Product

    @DELETE("product/deleteProduct")
    suspend fun deleteProduct(@Query("id") id: String)
}
package htt.catalogo.api

import htt.catalogo.logininstance.LoginInstance
import htt.catalogo.model.Product

class ApiRepo {
    private val apiService = ApiClient.retrofit.create(ApiService::class.java)

    suspend fun getAllProducts() = apiService.getAllProducts()

    suspend fun addProductToCart(email: String, id: String) = apiService.addProductToCart(email, id)

    suspend fun getCartProducts() = apiService.getCartProducts(LoginInstance.currentUser?.email ?: "")

    suspend fun removeProductFromCart(id: String) = apiService.removeProductFromCart(LoginInstance.currentUser?.email ?: "", id)

    suspend fun login(email: String, password: String) = apiService.login(email, password)

    suspend fun addProduct(product: Product) = apiService.addProduct(product)

    suspend fun deleteProduct(id: String) = apiService.deleteProduct(id)

    suspend fun confirmBuy(email: String) = apiService.confirmBuy(email)
}
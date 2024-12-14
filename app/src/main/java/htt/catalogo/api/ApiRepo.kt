package htt.catalogo.api

class ApiRepo {
    private val apiService = ApiClient.retrofit.create(ApiService::class.java)

    suspend fun getAllProducts() = apiService.getAllProducts()

    suspend fun addProductToCart(email: String, id: String) = apiService.addProductToCart(email, id)

    suspend fun getCartProducts() = apiService.getCartProducts("admin@gmail.com")

    suspend fun removeProductFromCart(id: String) = apiService.removeProductFromCart("admin@gmail.com", id)

    suspend fun login(email: String, password: String) = apiService.login(email, password)
}
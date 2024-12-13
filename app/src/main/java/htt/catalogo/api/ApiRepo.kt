package htt.catalogo.api
class ApiRepo {
    private val apiService = ApiClient.retrofit.create(ApiService::class.java)
    suspend fun getAllProducts() = apiService.getAllProducts()
}
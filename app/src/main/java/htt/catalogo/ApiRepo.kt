package htt.catalogo

import com.google.gson.GsonBuilder
import htt.catalogo.models.Product
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiRepo {
    private val apiService: ApiService

    init {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://localhost:8080/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    suspend fun getAllProducts(): List<Product> {
        return apiService.getAllProducts()
    }
}
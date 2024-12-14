package htt.catalogo.ui.home

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import htt.catalogo.R
import htt.catalogo.adapters.ProductAdapter
import htt.catalogo.api.ApiRepo
import htt.catalogo.model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductListActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val repository = ApiRepo()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val products = repository.getAllProducts()
                withContext(Dispatchers.Main) {
                    val adapter = ProductAdapter(products) { product ->
                        // Aquí se maneja el clic en el ícono del carrito
                        addToCart(product)
                    }
                    recyclerView.adapter = adapter
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Función que se llama cuando se hace clic en el ícono del carrito
    private fun addToCart(product: Product) {
        // Aquí puedes agregar el código para agregar el producto al carrito
        Toast.makeText(this, "${product.name} agregado al carrito", Toast.LENGTH_SHORT).show()

        // Lógica para agregar al carrito, por ejemplo:
        // - Guardar en una lista global del carrito
        // - Navegar a una pantalla de carrito
    }
}
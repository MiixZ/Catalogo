package htt.catalogo.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import htt.catalogo.R
import htt.catalogo.adapters.ProductAdapter
import htt.catalogo.api.ApiRepo
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
                    recyclerView.adapter = ProductAdapter(products)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
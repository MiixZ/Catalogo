package htt.catalogo.ui.productlist

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import htt.catalogo.R
import htt.catalogo.api.ApiRepo
import htt.catalogo.ui.map.MapActivity

class ProductListActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val buttonMap: Button = findViewById(R.id.button_map)
        buttonMap.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }

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
package htt.catalogo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import htt.catalogo.R
import htt.catalogo.adapters.ProductAdapter
import htt.catalogo.api.ApiRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_product_list, container, false)
        val repository = ApiRepo()

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = ProductAdapter(emptyList(), { product ->
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    repository.addProductToCart("admin@gmail.com", product.id.toString())
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "${product.name} agregado al carrito", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })

        recyclerView.adapter = adapter

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val products = repository.getAllProducts()
                withContext(Dispatchers.Main) {
                    adapter.updateProducts(products)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return view
    }
}
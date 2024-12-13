package htt.catalogo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Configura un adaptador vacío inicialmente
        val adapter = ProductAdapter(emptyList())
        recyclerView.adapter = adapter

        val repository = ApiRepo()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val products = repository.getAllProducts()
                withContext(Dispatchers.Main) {
                    // Actualiza el adaptador con los datos obtenidos
                    adapter.updateProducts(products)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return view
    }
}
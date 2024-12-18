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
import htt.catalogo.logininstance.LoginInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductListFragment : Fragment() {

    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_product_list, container, false)
        val repository = ApiRepo()

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = ProductAdapter(emptyList(), emptyList(), { product ->
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    repository.addProductToCart(LoginInstance.currentUser?.email ?: "", product.id.toString())
                    val cartProducts = repository.getCartProducts()
                    withContext(Dispatchers.Main) {
                        adapter.updateProducts(adapter.products, cartProducts)
                        Toast.makeText(requireContext(), "${product.name} agregado al carrito", Toast.LENGTH_SHORT).show()
                        requireActivity().recreate()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }, { product ->
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    repository.removeProductFromCart(product.id.toString())
                    val cartProducts = repository.getCartProducts()
                    withContext(Dispatchers.Main) {
                        adapter.updateProducts(adapter.products, cartProducts)
                        Toast.makeText(requireContext(), "${product.name} eliminado del carrito", Toast.LENGTH_SHORT).show()
                        requireActivity().recreate()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }, showTotal = false)

        recyclerView.adapter = adapter

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val products = repository.getAllProducts()
                val cartProducts = repository.getCartProducts()
                withContext(Dispatchers.Main) {
                    adapter.updateProducts(products, cartProducts)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        adapter.notifyDataSetChanged()

        adapter.showCartButtons = LoginInstance.currentUser != null
    }
}
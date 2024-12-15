package htt.catalogo.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import htt.catalogo.R
import htt.catalogo.adapters.ProductAdapter
import htt.catalogo.api.ApiRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartFragment: Fragment() {
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        val repository = ApiRepo()

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = ProductAdapter(emptyList(), emptyList(), { product ->

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
        })

        recyclerView.adapter = adapter

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val products = repository.getCartProducts()
                withContext(Dispatchers.Main) {
                    adapter.updateProducts(products, products)
                }

                if (products.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "No hay productos en el carrito", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val checkoutButton: Button = view.findViewById(R.id.checkout_button)
        checkoutButton.setOnClickListener {
            findNavController().navigate(R.id.checkoutFragment)
        }

        return view
    }
}
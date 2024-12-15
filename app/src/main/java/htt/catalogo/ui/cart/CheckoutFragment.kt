package htt.catalogo.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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

class CheckoutFragment : Fragment() {
    private lateinit var adapter: ProductAdapter
    private lateinit var totalPriceText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_checkout, container, false)
        val repository = ApiRepo()

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_checkout)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = ProductAdapter(emptyList(), emptyList(), { product -> }, { product -> }, showCartButtons = false)
        recyclerView.adapter = adapter

        totalPriceText = view.findViewById(R.id.total_price_text)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val products = repository.getCartProducts()
                withContext(Dispatchers.Main) {
                    adapter.updateProducts(products, products)
                    updateTotalPrice(products)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val confirmPurchaseButton: Button = view.findViewById(R.id.confirm_purchase_button)
        confirmPurchaseButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    repository.confirmBuy("admin@gmail.com")
                    withContext(Dispatchers.Main) {
                        adapter.updateProducts(emptyList(), emptyList())
                        updateTotalPrice(emptyList())
                        findNavController().navigate(R.id.nav_cart)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        return view
    }

    private fun updateTotalPrice(products: List<Product>) {
        val totalPrice = products.sumOf { it.price }
        totalPriceText.text = "Total: ${"%.2f".format(totalPrice)}"
    }
}
package htt.catalogo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import htt.catalogo.R
import htt.catalogo.model.Product

class ProductAdapter(
    private var products: List<Product>,
    private val onAddToCartClick: (Product) -> Unit // Función que maneja el clic en el carrito
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productName: TextView = itemView.findViewById(R.id.product_name)
        private val productPrice: TextView = itemView.findViewById(R.id.product_price)
        private val addToCartIcon: ImageView = itemView.findViewById(R.id.product_icon) // Aquí agregamos el icono del carrito

        fun bind(product: Product, onAddToCartClick: (Product) -> Unit) {
            productName.text = product.name
            productPrice.text = String.format("%s €", product.price)

            addToCartIcon.setOnClickListener {
                onAddToCartClick(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position], onAddToCartClick) // Pasamos la función aquí
    }

    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = products.size
}
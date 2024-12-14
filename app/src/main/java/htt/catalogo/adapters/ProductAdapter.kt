package htt.catalogo.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import htt.catalogo.R
import htt.catalogo.model.Product

class ProductAdapter(
    var products: List<Product>,
    private var cartProducts: List<Product>,
    private val onAddToCartClick: (Product) -> Unit,
    private val onDeleteFromCartClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product, cartProducts.contains(product))
    }

    override fun getItemCount(): Int = products.size

    fun updateProducts(newProducts: List<Product>, newCartProducts: List<Product>) {
        products = newProducts
        cartProducts = newCartProducts
        notifyDataSetChanged()
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productName: TextView = itemView.findViewById(R.id.product_name)
        private val productPrice: TextView = itemView.findViewById(R.id.product_price)
        private val addToCartIcon: ImageView = itemView.findViewById(R.id.product_icon)
        private val deleteFromCartIcon: ImageView = itemView.findViewById(R.id.delete_product_icon)

        @SuppressLint("SetTextI18n")
        fun bind(product: Product, isInCart: Boolean) {
            productName.text = product.name
            productPrice.text = product.price.toString()

            if (isInCart) {
                addToCartIcon.visibility = View.GONE
                deleteFromCartIcon.visibility = View.VISIBLE
                deleteFromCartIcon.setOnClickListener { onDeleteFromCartClick(product) }
            } else {
                addToCartIcon.visibility = View.VISIBLE
                deleteFromCartIcon.visibility = View.GONE
                addToCartIcon.setOnClickListener { onAddToCartClick(product) }
            }
        }
    }
}
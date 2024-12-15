package htt.catalogo.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import htt.catalogo.R
import htt.catalogo.api.ApiRepo
import htt.catalogo.model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductAdapter(
    var products: List<Product>,
    private var cartProducts: List<Product>,
    private val onAddToCartClick: (Product) -> Unit,
    private val onDeleteFromCartClick: (Product) -> Unit,
    private val showTotal: Boolean = true,
    private val showCartButtons: Boolean = true
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_PRODUCT = 0
        private const val VIEW_TYPE_TOTAL = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < products.size) VIEW_TYPE_PRODUCT else VIEW_TYPE_TOTAL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_PRODUCT) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
            ProductViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_total, parent, false)
            TotalViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProductViewHolder) {
            val product = products[position]
            holder.bind(product, cartProducts.contains(product))
        } else if (holder is TotalViewHolder) {
            holder.bind(cartProducts.sumOf { it.price })
        }
    }

    override fun getItemCount(): Int = if (showTotal) products.size + 1 else products.size

    fun updateProducts(newProducts: List<Product>, newCartProducts: List<Product>) {
        products = newProducts
        cartProducts = newCartProducts
        notifyDataSetChanged()
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productName: TextView = itemView.findViewById(R.id.product_name)
        private val productPrice: TextView = itemView.findViewById(R.id.product_price)
        private val productImage: ImageView = itemView.findViewById(R.id.product_image)
        private val addToCartIcon: ImageView = itemView.findViewById(R.id.product_icon)
        private val deleteFromCartIcon: ImageView = itemView.findViewById(R.id.delete_product_icon)
        private val trashProductIcon: ImageView = itemView.findViewById(R.id.trash_product)

        fun bind(product: Product, isInCart: Boolean) {
            productName.text = product.name
            productPrice.text = product.price.toString()
            val imageBytes = product.getImageBytes()
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            productImage.setImageBitmap(bitmap)

            if (showCartButtons) {
                if (isInCart) {
                    addToCartIcon.visibility = View.GONE
                    deleteFromCartIcon.visibility = View.VISIBLE
                    deleteFromCartIcon.setOnClickListener { onDeleteFromCartClick(product) }
                } else {
                    addToCartIcon.visibility = View.VISIBLE
                    deleteFromCartIcon.visibility = View.GONE
                    addToCartIcon.setOnClickListener { onAddToCartClick(product) }
                }
            } else {
                addToCartIcon.visibility = View.GONE
                deleteFromCartIcon.visibility = View.GONE
                trashProductIcon.visibility = View.GONE
            }

            trashProductIcon.setOnClickListener {
                val repository = ApiRepo()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        repository.deleteProduct(product.id.toString())
                        withContext(Dispatchers.Main) {
                            products = products.filter { it.id != product.id }
                            notifyDataSetChanged()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    inner class TotalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val totalPrice: TextView = itemView.findViewById(R.id.total_price)

        fun bind(total: Double) {
            totalPrice.text = "Total: $total"
        }
    }
}
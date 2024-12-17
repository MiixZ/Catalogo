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
import htt.catalogo.logininstance.LoginInstance
import htt.catalogo.model.Product
import kotlinx.coroutines.*

class ProductAdapter(
    var products: List<Product>,
    private var cartProducts: List<Product>,
    private val onAddToCartClick: (Product) -> Unit,
    private val onDeleteFromCartClick: (Product) -> Unit,
    private val showTotal: Boolean = true,
    var showCartButtons: Boolean = true,
    var onChart: Boolean = false
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_PRODUCT = 0
        private const val VIEW_TYPE_TOTAL = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < products.size) VIEW_TYPE_PRODUCT else VIEW_TYPE_TOTAL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = when (viewType) {
            VIEW_TYPE_PRODUCT -> layoutInflater.inflate(R.layout.item_product, parent, false)
            else -> layoutInflater.inflate(R.layout.item_total, parent, false)
        }
        return if (viewType == VIEW_TYPE_PRODUCT) ProductViewHolder(view) else TotalViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProductViewHolder) {
            holder.bind(products[position], cartProducts.contains(products[position]))
        } else if (holder is TotalViewHolder) {
            holder.bind(cartProducts.sumOf { it.price })
        }
    }

    override fun getItemCount(): Int = products.size + if (showTotal) 1 else 0

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
            productPrice.text = "$${product.price}"
            productImage.setImageBitmap(getProductBitmap(product))

            configureCartButtons(product, isInCart)
        }

        private fun getProductBitmap(product: Product) = BitmapFactory.decodeByteArray(
            product.getImageBytes(), 0, product.getImageBytes().size
        )

        private fun configureCartButtons(product: Product, isInCart: Boolean) {
            val isAdmin = LoginInstance.currentUser?.role == "ADMIN" && !onChart

            if (showCartButtons && LoginInstance.currentUser != null) {
                addToCartIcon.visibility = if (isInCart) View.GONE else View.VISIBLE
                deleteFromCartIcon.visibility = if (isInCart) View.VISIBLE else View.GONE
                trashProductIcon.visibility = if (isAdmin) View.VISIBLE else View.GONE

                addToCartIcon.setOnClickListener { onAddToCartClick(product) }
                deleteFromCartIcon.setOnClickListener { onDeleteFromCartClick(product) }
                trashProductIcon.setOnClickListener { deleteProduct(product) }
            } else {
                addToCartIcon.visibility = View.GONE
                deleteFromCartIcon.visibility = View.GONE
                trashProductIcon.visibility = View.GONE
            }
        }

        private fun deleteProduct(product: Product) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    ApiRepo().deleteProduct(product.id.toString())
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

    inner class TotalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val totalPrice: TextView = itemView.findViewById(R.id.total_price)

        fun bind(total: Double) {
            totalPrice.text = "Total: $total"
        }
    }
}

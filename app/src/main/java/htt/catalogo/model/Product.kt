package htt.catalogo.model

import android.util.Base64

data class Product(
    val id: Long,
    val name: String,
    val price: Double,
    val image: String
) {
    fun getImageBytes(): ByteArray {
        return Base64.decode(image, Base64.DEFAULT)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        if (id != other.id) return false
        if (name != other.name) return false
        if (price != other.price) return false
        if (image != other.image) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + image.hashCode()
        return result
    }
}
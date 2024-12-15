package htt.catalogo.ui.form

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import htt.catalogo.MainActivity
import htt.catalogo.R
import htt.catalogo.api.ApiRepo
import htt.catalogo.model.Product
import htt.catalogo.ui.home.ProductListActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class FormFragment : Fragment() {

    private lateinit var productNameEditText: EditText
    private lateinit var productPriceEditText: EditText
    private lateinit var productImageView: ImageView
    private lateinit var selectImageButton: Button
    private lateinit var submitButton: Button
    private var selectedImageBase64: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_form, container, false)

        productNameEditText = view.findViewById(R.id.product_name)
        productPriceEditText = view.findViewById(R.id.product_price)
        productImageView = view.findViewById(R.id.product_image)
        selectImageButton = view.findViewById(R.id.select_image_button)
        submitButton = view.findViewById(R.id.submit_button)

        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }

        submitButton.setOnClickListener {
            val name = productNameEditText.text.toString()
            val price = productPriceEditText.text.toString().toDoubleOrNull()

            if (name.isNotEmpty() && price != null && selectedImageBase64 != null) {
                addProduct(name, price, selectedImageBase64!!)
            } else {
                Toast.makeText(requireContext(), "Debes introducir todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            val inputStream = imageUri?.let { requireContext().contentResolver.openInputStream(it) }
            val bitmap = BitmapFactory.decodeStream(inputStream)
            productImageView.setImageBitmap(bitmap)
            selectedImageBase64 = bitmapToBase64(bitmap)
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun addProduct(name: String, price: Double, imageBase64: String) {
        val repository = ApiRepo()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val product = Product(0, name, price, imageBase64)
                repository.addProduct(product)
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Product added successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Failed to add product", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 1
    }
}
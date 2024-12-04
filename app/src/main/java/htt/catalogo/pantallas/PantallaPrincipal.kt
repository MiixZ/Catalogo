package htt.catalogo.pantallas

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import htt.catalogo.Greeting
import htt.catalogo.ui.theme.CatalogoTheme

@Composable
fun PantallaPrincipal(onNavigateToDetails: () -> Unit) {
    // Contenido de la pantalla principal
    Text(text = "¡Bienvenido al Catálogo!", modifier = Modifier)

    Button(onClick = onNavigateToDetails) {
        Text(text = "Explorar Productos")
    }

    /*
    val repository = ApiRepo()
    lifecycleScope.launch {
        try {
            val products = repository.getAllProducts() // Lista de productos desde la API
            //recyclerView.adapter = ProductAdapter(products)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

     */
}
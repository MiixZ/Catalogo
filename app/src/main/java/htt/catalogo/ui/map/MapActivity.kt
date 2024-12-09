package htt.catalogo.ui.map

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import htt.catalogo.R

class MapActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val recyclerView: RecyclerView = findViewById(R.id.activity_map_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}
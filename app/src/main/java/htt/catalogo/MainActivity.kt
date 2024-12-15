package htt.catalogo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import htt.catalogo.databinding.ActivityMainBinding
import htt.catalogo.logininstance.LoginInstance
import htt.catalogo.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val LOCATION_PERMISSION_REQUEST = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkLocationPermission()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.map, R.id.nav_cart, R.id.add_product),
            drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> navController.navigate(R.id.nav_home)
                R.id.map -> navController.navigate(R.id.map)
                R.id.nav_cart -> navController.navigate(R.id.nav_cart)
                R.id.add_product -> navController.navigate(R.id.formProduct)
            }
            drawerLayout.closeDrawers()
            true
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.nav_cart) {
                binding.appBarMain.fab.hide()
                binding.appBarMain.fabHome.show()
            } else {
                binding.appBarMain.fab.show()
                binding.appBarMain.fabHome.hide()
            }
        }

        updateHeader()

        binding.appBarMain.fab.setOnClickListener {
            navController.navigate(R.id.nav_cart)
        }
        binding.appBarMain.fabHome.setOnClickListener {
            navController.navigate(R.id.nav_home)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso de ubicación concedido", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_login -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_logout -> {
                LoginInstance.currentUser = null
                updateHeader()
                invalidateOptionsMenu()
                recreate()

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        updateHeader()

        val menu = binding.navView.menu
        val addProductItem = menu.findItem(R.id.add_product)
        addProductItem.isVisible = LoginInstance.currentUser?.role.equals("ADMIN")
    }

    private fun updateHeader() {
        val headerView = binding.navView.getHeaderView(0)
        val name: TextView = headerView.findViewById(R.id.name)
        val mail: TextView = headerView.findViewById(R.id.mail)

        name.text = LoginInstance.currentUser?.name ?: "Usuario no disponible"
        mail.text = LoginInstance.currentUser?.email ?: "Email no disponible"
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        super.onPrepareOptionsMenu(menu)

        val loginItem = menu.findItem(R.id.action_login)
        val logoutItem = menu.findItem(R.id.action_logout)

        if (LoginInstance.currentUser == null) {
            loginItem.isVisible = true
            logoutItem.isVisible = false
        } else {
            loginItem.isVisible = false
            logoutItem.isVisible = true
        }
        return true
    }
}

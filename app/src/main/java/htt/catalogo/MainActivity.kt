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

    companion object {
        private const val LOCATION_PERMISSION_REQUEST = 100
    }

    // --- Activity Lifecycle ---
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LoginInstance.loadUser(this)

        checkLocationPermission()
        setupUI()
    }

    override fun onResume() {
        super.onResume()
        updateHeader()
        toggleAdminMenuItem()
    }

    // --- UI Setup ---
    private fun setupUI() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        setupNavigation()
        setupFabButtons()
        updateHeader()
    }

    private fun setupNavigation() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.map, R.id.nav_cart, R.id.add_product),
            drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener { menuItem ->
            handleNavigationSelection(menuItem.itemId, navController)
            drawerLayout.closeDrawers()
            true
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            toggleFabVisibility(destination.id)
        }
    }

    // --- Floating Action Buttons (FABs) ---
    private fun setupFabButtons() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        binding.appBarMain.fab.setOnClickListener {
            navController.navigate(R.id.nav_cart)
        }
        binding.appBarMain.fabHome.setOnClickListener {
            navController.navigate(R.id.nav_home)
        }
    }

    private fun toggleFabVisibility(destinationId: Int) {
        if (destinationId == R.id.nav_cart) {
            binding.appBarMain.fab.hide()
            binding.appBarMain.fabHome.show()
        } else {
            binding.appBarMain.fab.show()
            binding.appBarMain.fabHome.hide()
        }
    }

    // --- Navigation Actions ---
    private fun handleNavigationSelection(itemId: Int, navController: androidx.navigation.NavController) {
        when (itemId) {
            R.id.nav_home -> navController.navigate(R.id.nav_home)
            R.id.map -> navController.navigate(R.id.map)
            R.id.nav_cart -> navController.navigate(R.id.nav_cart)
            R.id.add_product -> navController.navigate(R.id.formProduct)
        }
    }

    // --- Header Updates ---
    private fun updateHeader() {
        val headerView = binding.navView.getHeaderView(0)
        headerView.findViewById<TextView>(R.id.name).text =
            LoginInstance.currentUser?.name ?: "Usuario no disponible"
        headerView.findViewById<TextView>(R.id.mail).text =
            LoginInstance.currentUser?.email ?: "Email no disponible"
    }

    private fun toggleAdminMenuItem() {
        val isAdmin = LoginInstance.currentUser?.role == "ADMIN"
        val menu = binding.navView.menu
        menu.findItem(R.id.add_product).isVisible = isAdmin
    }

    // --- Options Menu Setup ---
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val loginItem = menu.findItem(R.id.action_login)
        val logoutItem = menu.findItem(R.id.action_logout)

        val isLoggedIn = LoginInstance.currentUser != null
        loginItem.isVisible = !isLoggedIn
        logoutItem.isVisible = isLoggedIn

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_login -> {
                startActivity(Intent(this, LoginActivity::class.java))
                true
            }
            R.id.action_logout -> {
                performLogout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun performLogout() {
        LoginInstance.logout(this)
        updateHeader()
        invalidateOptionsMenu()
        recreate()
    }

    // --- Location Permissions ---
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
            val message = if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                "Permiso de ubicación concedido"
            } else {
                "Permiso de ubicación denegado"
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    // --- Back Navigation ---
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}

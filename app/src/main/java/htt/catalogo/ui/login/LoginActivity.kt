package htt.catalogo.ui.login

import htt.catalogo.R
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import htt.catalogo.api.ApiRepo
import htt.catalogo.logininstance.LoginInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameField = findViewById(R.id.username)
        passwordField = findViewById(R.id.password)
        loginButton = findViewById(R.id.btn_login)

        loginButton.setOnClickListener {
            val username = usernameField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            performLogin(username, password)
        }
    }

    private fun performLogin(username: String, password: String) {
        val repository = ApiRepo()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = repository.login(username, password)

                withContext(Dispatchers.Main) {
                    if (result.email.isNotEmpty()) {
                        Toast.makeText(this@LoginActivity, "Login exitoso", Toast.LENGTH_SHORT).show()
                        LoginInstance.currentUser = result
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Mail o contraseña erronea", Toast.LENGTH_LONG).show()
                    }
                }


                println(result)
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, "Mail o contraseña erronea", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

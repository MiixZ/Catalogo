package htt.catalogo.ui.login

import htt.catalogo.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import htt.catalogo.api.ApiRepo
import htt.catalogo.logininstance.LoginInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginFragment : Fragment() {
    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText
    private lateinit var loginButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        usernameField = view.findViewById(R.id.username)
        passwordField = view.findViewById(R.id.password)
        loginButton = view.findViewById(R.id.btn_login)

        loginButton.setOnClickListener {
            val username = usernameField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            performLogin(username, password)
        }

        return view
    }

    private fun performLogin(username: String, password: String) {
        val repository = ApiRepo()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = repository.login(username, password)

                withContext(Dispatchers.Main) {
                    if (result.email.isNotEmpty()) {
                        Toast.makeText(requireContext(), "Login exitoso", Toast.LENGTH_SHORT).show()
                        LoginInstance.currentUser = result
                        //findNavController().navigate(R.id.home_fragment_container)

                        requireActivity().finish()
                    } else {
                        Toast.makeText(requireContext(), "Mail o contraseña erronea", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Mail o contraseña erronea", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
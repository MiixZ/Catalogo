package htt.catalogo.logininstance

import android.content.Context
import com.google.gson.Gson
import htt.catalogo.model.User

object LoginInstance {
    var currentUser: User? = null

    fun saveUser(context: Context, user: User) {
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val userJson = gson.toJson(user)
        editor.putString("currentUser", userJson)
        editor.apply()
        currentUser = user
    }

    fun loadUser(context: Context) {
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val userJson = sharedPreferences.getString("currentUser", null)
        currentUser = if (userJson != null) {
            gson.fromJson(userJson, User::class.java)
        } else {
            null
        }
    }


    fun logout(context: Context) {
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("currentUser")
        editor.apply()
        currentUser = null
    }
}


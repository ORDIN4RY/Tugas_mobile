package com.example.sqliteproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import android.widget.Button
import com.example.sqliteproject.R
import androidx.core.view.WindowInsetsCompat
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.sqliteproject.LoginRequest
import com.example.sqliteproject.LoginResponse
import com.example.sqliteproject.DatabaseHelper
import com.example.sqliteproject.ApiService
import com.example.sqliteproject.RetrofitClient

class LoginActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var db: DatabaseHelper
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        db = DatabaseHelper(this)
        etUsername = findViewById<EditText>(R.id.inputEmail)
        etPassword = findViewById<EditText>(R.id.inputPass)
        btnLogin = findViewById<Button>(R.id.btnLogin)
        btnRegister = findViewById<TextView>(R.id.keRegis)


        // Cek apakah user sudah login
        db.getUser()?.let {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }
        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Isi semua kolom!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            loginUser(username, password)

        }

        btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }


    }

    fun loginUser(username: String, password: String) {
        val request = LoginRequest(username, password)
        RetrofitClient.instance.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful && response.body()?.status == "success") {
                    val id = response.body()?.user_id
                    val username = response.body()?.username
                    val token = response.body()?.token
                    if (id != null) {
                        db.clearUser()

                        db.saveUser(User(id, username!!, token!!))
                        Toast.makeText(
                            this@LoginActivity,
                            "Login berhasil",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(
                            Intent(
                                this@LoginActivity,
                                DashboardActivity::class.java
                            )
                        )
                        finish()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Login gagal", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                Toast.makeText(
//                    this@LoginActivity,
//                    t.message,
//                    Toast.LENGTH_SHORT
//                ).show()
                etUsername.setText(t.message)
            }
        })
    }

}

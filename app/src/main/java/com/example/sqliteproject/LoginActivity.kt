package com.example.sqliteproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import android.widget.Button
import com.example.sqliteproject.R
import androidx.core.view.WindowInsetsCompat
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityOptionsCompat
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
    private lateinit var loginCard: CardView
    private lateinit var tombolBack: ImageButton
    private lateinit var tombolShow: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        db = DatabaseHelper(this)
        etUsername = findViewById(R.id.inputEmail)
        etPassword = findViewById(R.id.inputPass)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.keRegis)
        loginCard = findViewById(R.id.cardlogin)
        tombolBack = findViewById(R.id.back)
        tombolShow = findViewById(R.id.btn_show)

        loginCard.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(400)
            .start()

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
            goToRegister()
        }

        tombolBack.setOnClickListener {
            goBack()
        }

        tombolShow.setOnClickListener {
            if(etPassword.inputType == 129){
                etPassword.inputType = 1
                tombolShow.setImageResource(R.drawable.eye_hide)
            }else{
                etPassword.inputType = 129
                tombolShow.setImageResource(R.drawable.eye_show)
            }
        }

    }

    fun loginUser(username: String, password: String) {
        val request = LoginRequest(username, password)

        RetrofitClient.instance.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                val body = response.body()

                if (response.isSuccessful && body != null) {
                    when (body.code) {
                        200 -> {
                            // Login sukses
                            body.user?.let { user ->
                                db.clearUser()
                                db.saveUser(user)

                                Toast.makeText(
                                    this@LoginActivity,
                                    "Login berhasil",
                                    Toast.LENGTH_SHORT
                                ).show()

                                startActivity(
                                    Intent(this@LoginActivity, DashboardActivity::class.java)
                                )
                                finish()
                            } ?: run {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Data user tidak ditemukan di response",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        401 -> {
                            Toast.makeText(
                                this@LoginActivity,
                                "Username atau password salah",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        400 -> {
                            Toast.makeText(
                                this@LoginActivity,
                                "Field tidak boleh kosong",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {
                            Toast.makeText(
                                this@LoginActivity,
                                "Terjadi kesalahan: ${body.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Response tidak valid atau gagal: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(
                    this@LoginActivity,
                    "Koneksi gagal: ${t.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    private fun goToRegister(){
        animateOut {
            val intent = Intent(this, RegisterActivity::class.java)

            val options = ActivityOptionsCompat.makeCustomAnimation(
                this,                // Context
                R.anim.fade_in,      // animasi masuk
                R.anim.fade_out      // animasi keluar
            )

            startActivity(intent, options.toBundle())
            this.finish()
        }
    }

    private fun goBack(){
        animateOut {
            val intent = Intent(this, MainActivity::class.java)

            val options = ActivityOptionsCompat.makeCustomAnimation(
                this,                // Context
                R.anim.fade_in,
                R.anim.fade_out
            )

            startActivity(intent, options.toBundle())
            this.finish()
        }
    }

    private fun animateOut(onEnd: () -> Unit) {
        loginCard.animate()
            .translationY(200f)
            .alpha(0f)
            .setDuration(200)
            .withEndAction(onEnd)
            .start()
    }



}

package com.example.sqliteproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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
import com.example.sqliteproject.R
import com.example.sqliteproject.RetrofitClient
class RegisterActivity : AppCompatActivity() {

    private lateinit var edtUser: EditText
    private lateinit var edtPass: EditText
    private lateinit var btnRegister: Button
    private lateinit var btnLogin: TextView
    private lateinit var regisCard : CardView
    private lateinit var tombolBack: ImageButton
    private lateinit var tombolShow: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        edtUser = findViewById(R.id.inputEmail)
        edtPass = findViewById(R.id.inputPass)
        btnRegister = findViewById(R.id.btnRegister)
        btnLogin = findViewById(R.id.keLogin)
        regisCard = findViewById(R.id.cardRegis)
        tombolBack = findViewById(R.id.back)
        tombolShow = findViewById(R.id.btn_show)


        regisCard.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(400)
            .start()

        btnRegister.setOnClickListener {
            val username = edtUser.text.toString().trim()
            val password = edtPass.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Isi semua field", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            registerUser(username, password)
        }

        btnLogin.setOnClickListener {
            goToLogin()
        }

        tombolBack.setOnClickListener {
            goBack()
        }
        tombolShow.setOnClickListener {
            if(edtPass.inputType == 129){
                edtPass.inputType = 1
                tombolShow.setImageResource(R.drawable.eye_hide)
            }else{
                edtPass.inputType = 129
                tombolShow.setImageResource(R.drawable.eye_show)
            }
        }
    }

    fun registerUser(username: String, password: String) {
        val request = RegisterRequest(username, password)

        RetrofitClient.instance.register(request).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful && response.body()?.status == "success") {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registrasi sukses!",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(
                        Intent(
                            this@RegisterActivity,
                            LoginActivity::class.java
                        )
                    )
                    finish()
                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        response.body()?.message ?: "Gagal registrasi",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(
                    this@RegisterActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun goToLogin(){
        animateOut {
            val intent = Intent(this, LoginActivity::class.java)

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
        regisCard.animate()
            .translationY(200f)
            .alpha(0f)
            .setDuration(200)
            .withEndAction(onEnd)
            .start()
    }
}

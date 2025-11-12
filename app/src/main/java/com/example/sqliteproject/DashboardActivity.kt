package com.example.sqliteproject

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityOptionsCompat
import com.example.sqliteproject.R
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardActivity : AppCompatActivity() {
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        var navbar = findViewById<BottomNavigationView>(R.id.bottomNav)
        db = DatabaseHelper(this)


        navbar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    Toast.makeText(this, "fragment Home", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.menu -> {
                    Toast.makeText(this, "fragment Menu", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.order -> {
                    Toast.makeText(this, "fragment Pesanan", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.logout -> {
                    db.clearUser().let {
                        val intent =
                            Intent(this@DashboardActivity, LoginActivity::class.java)

                        val options = ActivityOptionsCompat.makeCustomAnimation(
                            this@DashboardActivity,                // Context
                            R.anim.fade_in,      // animasi masuk
                            R.anim.fade_out      // animasi keluar
                        )

                        startActivity(intent, options.toBundle())
                        this@DashboardActivity.finish()
                    }
                    true
                }

                else -> false
            }
        }

    }
}

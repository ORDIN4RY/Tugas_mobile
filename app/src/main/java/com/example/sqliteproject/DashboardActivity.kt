package com.example.sqliteproject

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat

import android.content.Intent
import android.widget.Toast
import com.example.sqliteproject.R
import androidx.core.view.WindowInsetsCompat

class DashboardActivity : AppCompatActivity() {
    private lateinit var btnLogout : Button
    private lateinit var db : DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        btnLogout = findViewById<Button>(R.id.btnLogout)
        db = DatabaseHelper(this)


        btnLogout.setOnClickListener {
            db.clearUser()?.let {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }

        }

    }
    }

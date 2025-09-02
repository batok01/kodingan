package com.example.batok

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvWelcome = findViewById<TextView>(R.id.Welc)
        val btnLogout = findViewById<Button>(R.id.btnout)
        val btnCustomer = findViewById<Button>(R.id.btnCust)

        val username = intent.getStringExtra("username") ?: "User"
        tvWelcome.text = "Selamat Datang, $username"

        btnCustomer.setOnClickListener {
            val intent = Intent(this, halaman_customer::class.java)
            startActivity(intent)
            finish()
        }

        btnLogout.setOnClickListener {
            Toast.makeText(this, "Berhasil logout", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

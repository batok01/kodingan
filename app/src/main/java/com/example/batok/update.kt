package com.example.batok

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import com.example.batok.R
import com.example.batok.databaseHelper


class Update : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etDob: EditText
    private lateinit var etPhone: EditText
    private lateinit var etEmail: EditText
    private lateinit var etBankAccount: EditText
    private lateinit var btnSave: Button
    private lateinit var dbHelper: databaseHelper
    private var customerId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        // Inisialisasi view
        etName = findViewById(R.id.etName)
        etDob = findViewById(R.id.etDob)
        etPhone = findViewById(R.id.etPhone)
        etEmail = findViewById(R.id.etEmail)
        etBankAccount = findViewById(R.id.etBankAccount)
        btnSave = findViewById(R.id.btnSave)
        dbHelper = databaseHelper(this)

        // Ambil ID customer dari intent
        customerId = intent.getIntExtra("customerId", -1)

        if (customerId != -1) {
            // Ambil data customer untuk ditampilkan di inputan
            val customer = dbHelper.getCustomerById(customerId)
            etName.setText(customer["name"])
            etDob.setText(customer["dob"])
            etPhone.setText(customer["phone"])
            etEmail.setText(customer["email"])
            etBankAccount.setText(customer["bank_account"])
        }

        // Action button Save
        btnSave.setOnClickListener {
            val name = etName.text.toString()
            val dob = etDob.text.toString()
            val phone = etPhone.text.toString()
            val email = etEmail.text.toString()
            val bankAccount = etBankAccount.text.toString()

            if (name.isEmpty() || dob.isEmpty() || phone.isEmpty() || email.isEmpty() || bankAccount.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Update customer
                val success = dbHelper.updateCustomer(customerId, name, dob, phone, email, bankAccount)
                if (success) {
                    Toast.makeText(this, "Customer updated successfully", Toast.LENGTH_SHORT).show()
                    finish()  // Kembali ke halaman sebelumnya
                } else {
                    Toast.makeText(this, "Failed to update customer", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

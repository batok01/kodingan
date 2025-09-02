package com.example.batok

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class halaman_customer : AppCompatActivity() {

    private lateinit var btnCreateCustomer: Button
    private lateinit var etSearch: EditText
    private lateinit var customerListLayout: LinearLayout
    private lateinit var dbHelper: databaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_halaman_customer)

        btnCreateCustomer = findViewById(R.id.btnCreateCustomer)
        etSearch = findViewById(R.id.etSearch)
        customerListLayout = findViewById(R.id.customerListLayout)
        dbHelper = databaseHelper(this)

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                loadCustomers(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnCreateCustomer.setOnClickListener {
            val intent = Intent(this, create_customer::class.java)
            startActivity(intent)
            finish()
        }

        loadCustomers()
    }

    private fun loadCustomers(query: String = "") {
        val inflater = LayoutInflater.from(this)
        customerListLayout.removeAllViews()

        val customerList = dbHelper.getAllCustomers()

        val filteredList = if (query.isEmpty()) {
            customerList
        } else {
            customerList.filter {
                it["name"]?.contains(query, ignoreCase = true) == true
            }
        }

        for (customer in filteredList) {
            val cardView = inflater.inflate(R.layout.activity_item_customer, customerListLayout, false) as CardView

            val nameText = cardView.findViewById<TextView>(R.id.tvCustomerName)
            val dobText = cardView.findViewById<TextView>(R.id.tvCustomerDob)
            val phoneText = cardView.findViewById<TextView>(R.id.tvCustomerPhone)
            val emailText = cardView.findViewById<TextView>(R.id.tvCustomerEmail)
            val accountText = cardView.findViewById<TextView>(R.id.tvCustomerAccount)

            nameText.text = customer["name"]
            dobText.text = "Date of Birth\n${customer["dob"]}"
            phoneText.text = "Phone Number\n${customer["phone"]}"
            emailText.text = "Email\n${customer["email"]}"
            accountText.text = "Bank Account Number\n${customer["bank_account"]}"

            val btnDelete = cardView.findViewById<ImageView>(R.id.btnDelete)
            btnDelete.setOnClickListener {
                AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to delete this customer?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { _, _ ->
                        val customerId = customer["id"]?.toInt() ?: return@setPositiveButton
                        val success = dbHelper.deleteCustomer(customerId)
                        if (success) {
                            Toast.makeText(this, "Customer deleted successfully", Toast.LENGTH_SHORT).show()
                            loadCustomers()
                        } else {
                            Toast.makeText(this, "Failed to delete customer", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                    .show()
            }

            val btnEdit = cardView.findViewById<ImageView>(R.id.btnEdit)
            btnEdit.setOnClickListener {
                val intent = Intent(this, Update::class.java)
                intent.putExtra("customerId", customer["id"]?.toInt())
                startActivity(intent)
            }

            customerListLayout.addView(cardView)
        }
    }
}

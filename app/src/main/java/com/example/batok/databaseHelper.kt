package com.example.batok

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class databaseHelper(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {

    companion object {
        private const val DATABASE_NAME = "batok"
        private const val DATABASE_VERSION = 2

        // User table (uji)
        private const val TABLE_UJI = "uji"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"

        // Customer table
        private const val TABLE_CUSTOMER = "customer"
        private const val COLUMN_CUSTOMER_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_DOB = "dob"
        private const val COLUMN_PHONE = "phone"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_BANK_ACCOUNT = "bank_account"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUserTable = """
            CREATE TABLE $TABLE_UJI (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USERNAME TEXT NOT NULL,
                $COLUMN_PASSWORD TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createUserTable)

        // Sample users
        val sampleInsert = """
            INSERT INTO $TABLE_UJI ($COLUMN_USERNAME, $COLUMN_PASSWORD)
            VALUES ('admin', '12345'), ('username', 'password')
        """
        db.execSQL(sampleInsert)

        val createCustomerTable = """
            CREATE TABLE $TABLE_CUSTOMER (
                $COLUMN_CUSTOMER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_DOB TEXT,
                $COLUMN_PHONE TEXT,
                $COLUMN_EMAIL TEXT,
                $COLUMN_BANK_ACCOUNT TEXT
            )
        """.trimIndent()
        db.execSQL(createCustomerTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_UJI")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CUSTOMER")
        onCreate(db)
    }

    // User methods
    fun checkUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_UJI WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(username, password))

        val userExists = cursor.count > 0

        cursor.close()
        db.close()
        return userExists
    }

    fun insertUji(username: String, password: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
        }
        val result = db.insert(TABLE_UJI, null, values)
        db.close()
        return result != -1L
    }

    // Customer methods
    fun insertCustomer(
        name: String,
        dob: String,
        phone: String,
        email: String,
        bankAccount: String
    ): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_DOB, dob)
            put(COLUMN_PHONE, phone)
            put(COLUMN_EMAIL, email)
            put(COLUMN_BANK_ACCOUNT, bankAccount)
        }
        val result = db.insert(TABLE_CUSTOMER, null, values)
        db.close()
        return result != -1L
    }

    fun getAllCustomers(): List<Map<String, String>> {
        val db = readableDatabase
        val customerList = mutableListOf<Map<String, String>>()

        val cursor = db.rawQuery("SELECT * FROM $TABLE_CUSTOMER", null)
        if (cursor.moveToFirst()) {
            do {
                val customer = mapOf(
                    COLUMN_CUSTOMER_ID to cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CUSTOMER_ID)).toString(),
                    COLUMN_NAME to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    COLUMN_DOB to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DOB)),
                    COLUMN_PHONE to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                    COLUMN_EMAIL to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    COLUMN_BANK_ACCOUNT to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BANK_ACCOUNT))
                )
                customerList.add(customer)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return customerList
    }

    fun getCustomerById(customerId: Int): Map<String, String> {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_CUSTOMER,
            null,
            "$COLUMN_CUSTOMER_ID = ?",
            arrayOf(customerId.toString()),
            null,
            null,
            null
        )

        val customer = mutableMapOf<String, String>()

        if (cursor.moveToFirst()) {
            customer[COLUMN_CUSTOMER_ID] = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CUSTOMER_ID)).toString()
            customer[COLUMN_NAME] = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            customer[COLUMN_DOB] = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DOB))
            customer[COLUMN_PHONE] = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE))
            customer[COLUMN_EMAIL] = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
            customer[COLUMN_BANK_ACCOUNT] = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BANK_ACCOUNT))
        }

        cursor.close()
        db.close()
        return customer
    }

    fun updateCustomer(
        id: Int,
        name: String,
        dob: String,
        phone: String,
        email: String,
        bankAccount: String
    ): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_DOB, dob)
            put(COLUMN_PHONE, phone)
            put(COLUMN_EMAIL, email)
            put(COLUMN_BANK_ACCOUNT, bankAccount)
        }
        val result = db.update(TABLE_CUSTOMER, values, "$COLUMN_CUSTOMER_ID = ?", arrayOf(id.toString()))
        db.close()
        return result > 0
    }

    fun deleteCustomer(id: Int): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_CUSTOMER, "$COLUMN_CUSTOMER_ID = ?", arrayOf(id.toString()))
        db.close()
        return result > 0
    }
}

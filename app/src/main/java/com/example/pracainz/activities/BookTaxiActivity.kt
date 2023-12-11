package com.example.pracainz.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pracainz.R

import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.pracainz.models.Taxi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class BookTaxiActivity : AppCompatActivity() {

    private lateinit var editTextTaxiNumber: EditText
    private lateinit var editTextDriverName: EditText
    private lateinit var editTextAddress: EditText
    private lateinit var editTextPhoneNumber: EditText
    private lateinit var buttonSaveTaxi: Button
    var uid_d: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_taxi)

        var name = intent.getStringExtra("d_name" )
        val phone = intent.getStringExtra("phone_no")
        uid_d = intent.getStringExtra("driver_id")
        // Initialize UI elements
        editTextTaxiNumber = findViewById(R.id.editTextTaxiNumber)
        editTextDriverName = findViewById(R.id.editTextDriverName)
        editTextAddress = findViewById(R.id.editTextAddress)
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber)
        buttonSaveTaxi = findViewById(R.id.buttonSaveTaxi)
        editTextDriverName.setText(name)
        editTextTaxiNumber.setText(phone)


        // Set click listener for Save Taxi button
        buttonSaveTaxi.setOnClickListener {
            saveTaxiData()
        }
    }

    private fun saveTaxiData() {
        // Get values from EditTexts
        val taxiNumber = editTextTaxiNumber.text.toString()
        val driverName = editTextDriverName.text.toString()
        val address = editTextAddress.text.toString()
        val phoneNumber = editTextPhoneNumber.text.toString()

        // Validate data (you can add your own validation logic)
        val uid = FirebaseAuth.getInstance().uid ?: ""

        // Save data to Firebase Realtime Database
        val databaseReference = FirebaseDatabase.getInstance().getReference("taxis")
        if (uid_d != null) {
            val taxi = Taxi(uid,uid_d, taxiNumber, driverName, address, phoneNumber)
            databaseReference.child(uid_d).setValue(taxi)
            Toast.makeText(this, "Taxi Book Successfully", Toast.LENGTH_SHORT).show()
            // Display a success message or navigate to another screen
        }
    }
}

package com.example.pracainz.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.pracainz.R
import com.example.pracainz.models.User
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private val LOCATION_PERMISSION_REQUEST = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var roleselection = 0
    var lat: Double = 0.0
    var lng: Double = 0.0
    var user: Int? = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        spinnerRole.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                user=roleselection
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                roleselection = p2
                user=roleselection

            }
        }

        registerButtonConfirm.setOnClickListener {
            checkLocationPermissionAndPerformRegister()
        }
    }

    private fun checkLocationPermissionAndPerformRegister() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST
            )
        } else {
            performRegister()
        }
    }

    private fun performRegister() {
        val login = emailTextRegister.text.toString()
        val password = passwordTextRegister.text.toString()
        val repeatpassword = passwordRepeatTextRegister.text.toString()
        val phone = phoneTextView.text.toString()

        if (login.isEmpty() || password.isEmpty() || repeatpassword.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (repeatpassword != password) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            val address = "Lat: ${location?.latitude ?: 0.0}, Lng: ${location?.longitude ?: 0.0}"
             lat = location?.latitude ?: 0.0
             lng = location?.longitude ?: 0.0
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(login, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        addressTextView.setText(address)
                        saveUserToDatabase(address)
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to Create User", Toast.LENGTH_SHORT).show()
                    }
                }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to get location", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveUserToDatabase(address: String) {
        val username = usernameText.text.toString()
        val phone = phoneTextView.text.toString()
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("users/$uid")
        val user = User(uid, username, roleselection, false, false, phone, lat,lng, user!!)
        ref.setValue(user).addOnSuccessListener {
            Toast.makeText(this, "New User Added", Toast.LENGTH_SHORT).show()
        }
    }

    // Handle the result of the location permission request
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    performRegister()
                } else {
                    Toast.makeText(
                        this,
                        "Location permission denied. Unable to get current location.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }
        }
    }
}

package com.example.pracainz.activities

import android.Manifest
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.pracainz.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val LOCATION_PERMISSION_REQUEST_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (ContextCompat.checkSelfPermission(
               this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request location permissions
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission already granted, you can proceed with your code
//            setupMap()
        }
        loginButton.setOnClickListener {
            performLogin()
        }

        registerButtonGo.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent) }


    }
    // Handle permission request result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Location permission granted, you can proceed with your code
//                    setupMap()
                } else {
                    // Location permission denied, handle it accordingly (show a message, etc.)
                    // You may want to disable features that require location permissions
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
    private fun userTypeDirecting(loggedUser:Int){

        if(loggedUser==0){
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
            Log.d("brawo",loggedUser.toString())
            finish()
        }
        else if(loggedUser==1){
            val intent = Intent(this, DriveActivity::class.java)
            startActivity(intent)
            Log.d("brawo",loggedUser.toString())
            finish()
        }
        else{
            Toast.makeText(this,"Zly typ uzytkownika", Toast.LENGTH_SHORT).show()
        }
    }
    private fun performLogin(){
        val login=emailTextRegister.text.toString()
        val password=passwordTextRegister.text.toString()
        if(login.isEmpty()||password.isEmpty()) {

            Toast.makeText(this,"Please enter email/password", Toast.LENGTH_SHORT).show()
            return

        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(login,password).addOnCompleteListener {
            if(!it.isSuccessful)return@addOnCompleteListener
            val uid= FirebaseAuth.getInstance().uid?: ""
            val ref=FirebaseDatabase.getInstance().getReference("/users/$uid/isDriver")
            ref.addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                @SuppressLint("SuspiciousIndentation")
                override fun onDataChange(p0: DataSnapshot) {
                  var loggedUser=p0.getValue(Int::class.java)
                       userTypeDirecting(loggedUser!!)

                }

            })


        }.addOnFailureListener {
            Toast.makeText(this,"Login failed", Toast.LENGTH_SHORT).show()
        }
    }

}
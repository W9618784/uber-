package com.example.pracainz.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pracainz.R
import android.widget.TextView
import com.example.pracainz.models.User
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userId: String // Assume you set the user ID in this variable

    private lateinit var usernameTextView: TextView
    private lateinit var phoneTextView: TextView
    private lateinit var roleTextView: TextView // Assuming you want to display the role

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) {
             userId = firebaseUser.uid
            // Now, userId contains the unique identifier of the current user
        } else {
            // Handle the case where the user is not authenticated
        }
        // Initialize TextViews
        usernameTextView = view.findViewById(R.id.usernameTextView)
        phoneTextView = view.findViewById(R.id.phoneTextView)
        roleTextView = view.findViewById(R.id.roleTextView)

        // Retrieve user data based on user ID
        retrieveUserData(userId)

        return view
    }

    private fun retrieveUserData(userId: String) {
        val usersRef = database.getReference("users").child(userId)

        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                user?.let {
                    // Update UI with user data
                    updateUI(user)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })
    }

    private fun updateUI(user: User) {
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            val location = LatLng(user.lat, user.lng)

            // Add a marker at the driver's location
            googleMap.addMarker(MarkerOptions().position(location).title("Your Current Location"))

            // Move the camera to the driver's location and set a desired zoom level (e.g., 15f)
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 15f)
            googleMap.moveCamera(cameraUpdate)
            // Update TextViews with user data
            usernameTextView.text = "Username: ${user.username}"
            phoneTextView.text = "Phone: ${user.phone}"
            roleTextView.text = "Role: ${getRoleString(user.isDriver)}"
        }
    // Assuming you have a function to get the role string
    }

    private fun getRoleString(roleSelection: Int): String {
        // Implement your logic to convert roleSelection to a readable string
        return when (roleSelection) {
            0 -> "Role 0"
            1 -> "Role 1"
            // Add more cases if needed
            else -> "Unknown Role"
        }
    }
}

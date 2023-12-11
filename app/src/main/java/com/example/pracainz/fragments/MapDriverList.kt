package com.example.pracainz.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pracainz.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pracainz.adapters.DriverListAdapter
import com.example.pracainz.models.User
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MapDriverList : Fragment(), OnMapReadyCallback {
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    private lateinit var googleMap: GoogleMap
    private lateinit var driverListAdapter: DriverListAdapter
    private val driverList: MutableList<User> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map_driver_list, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment1) as? SupportMapFragment
        if (mapFragment != null) {
            mapFragment.getMapAsync(this)
        } else {
            // Handle the case where the map fragment is not found
        }

        // Initialize RecyclerView and its adapter
//        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
//        driverListAdapter = DriverListAdapter(requireContext(), driverList)
//
//        recyclerView.layoutManager = LinearLayoutManager(context)
//        recyclerView.adapter = driverListAdapter

        // Retrieve list of drivers
        retrieveDriverList()

        return view
    }

    override fun onMapReady(gMap: GoogleMap) {
        googleMap = gMap
        showMarkers()
    }

    private fun showMarkers() {
        if (driverList.isEmpty()) {
            // Handle the case when there are no drivers to display
            return
        }

        for (user in driverList) {
            val location = LatLng(user.lat, user.lng)
          //  Toast.makeText(context, ""+user.lat, Toast.LENGTH_SHORT).show()
            googleMap.addMarker(MarkerOptions().position(location).title(user.username))
        }

        // Move the camera to show all markers
        val builder = LatLngBounds.Builder()
        for (user in driverList) {
            builder.include(LatLng(user.lat, user.lng))
        }

        val bounds = builder.build()

//        if (bounds.contains(null)) {
//            // Handle the case when there are no valid bounds
//            return
//        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50))
    }

    private fun retrieveDriverList() {
        val usersRef = database.getReference("users")
        val query: Query = usersRef.orderByChild("isDriver").equalTo(1.0)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                driverList.clear()

                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    user?.let {
                        driverList.add(it)
                    }
                }

            showMarkers()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
                Toast.makeText(
                    context,
                    "Error retrieving data: ${databaseError.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}

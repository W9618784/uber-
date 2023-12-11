package com.example.pracainz.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pracainz.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_details)

        val lat = intent.getDoubleExtra("LAT_EXTRA", 0.0)
        val lng = intent.getDoubleExtra("LNG_EXTRA", 0.0)

        // Now you have the lat and lng, you can use them as needed
        // For example, you can show them on a Google Map
        // (Assuming you have a Google Map fragment in your layout with id "mapFragment")

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            val location = LatLng(lat, lng)

            // Add a marker at the driver's location
            googleMap.addMarker(MarkerOptions().position(location).title("Driver Location"))

            // Move the camera to the driver's location and set a desired zoom level (e.g., 15f)
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 15f)
            googleMap.moveCamera(cameraUpdate)
        }
    }
}
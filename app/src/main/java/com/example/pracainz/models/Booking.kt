package com.example.pracainz.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Booking(
    // Your existing fields
    val taxiNumber: String? = null,
    val driverName: String? = null,
    val address: String? = null,
    val phoneNumber: String? = null,
    val driId: String? = null,
    val taxiId: String? = null
) {
    // Add a no-argument constructor
    constructor() : this("", "", "", "","","")
}

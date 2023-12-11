package com.example.pracainz.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pracainz.R
import com.example.pracainz.adapters.BookingAdapter
import com.example.pracainz.models.Booking
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class OrdersList : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var bookingAdapter: BookingAdapter
    private val bookingList: MutableList<Booking> = mutableListOf()

    private lateinit var databaseReference: DatabaseReference
    private val uid = FirebaseAuth.getInstance().uid ?: ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_orders_list, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        bookingAdapter = BookingAdapter(bookingList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = bookingAdapter

        // Update the database reference to listen to the specific user's bookings
        databaseReference = FirebaseDatabase.getInstance().getReference("taxis")

        retrieveBookings()

        return view
    }

    private fun retrieveBookings() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                bookingList.clear()

                for (bookingSnapshot in dataSnapshot.children) {
                    val booking = bookingSnapshot.getValue(Booking::class.java)
                    booking?.let {
                        // Check if the user ID matches the taxi ID or driver ID
                        if (it.taxiId == uid || it.driId == uid) {
                            bookingList.add(it)
                        }
                    }
                }

                bookingAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })
    }
}

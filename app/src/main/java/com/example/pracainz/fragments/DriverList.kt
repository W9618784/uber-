package com.example.pracainz.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DriverList : Fragment() {
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    private lateinit var recyclerView: RecyclerView
    private lateinit var driverListAdapter: DriverListAdapter
    private val driverList: MutableList<User> = mutableListOf()


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_driver_list, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // Initialize RecyclerView and its adapter
        recyclerView = view.findViewById(R.id.recyclerView)
        driverListAdapter = DriverListAdapter(requireContext(), driverList)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = driverListAdapter

        // Retrieve list of drivers
        retrieveDriverList()

        return view
    }



    private fun retrieveDriverList() {
        val usersRef = database.getReference("users")

//        val query: Query = usersRef.orderByChild("isDriver").equalTo(1)
        val query: Query = usersRef.orderByChild("isDriver").equalTo(1.0)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                driverList.clear()
//                Toast.makeText(context, "eeggge"+driverList.get(0).user, Toast.LENGTH_SHORT).show()

                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    user?.let {
                        driverList.add(it)
//                        Toast.makeText(context, "data"+driverList.get(0).user, Toast.LENGTH_SHORT).show()
                    }
                }
                // Notify the adapter that the data set has changed
                driverListAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()

                Log.e("YourFragment", "Error retrieving data: ${databaseError.message}")
            }
        })
    }
}

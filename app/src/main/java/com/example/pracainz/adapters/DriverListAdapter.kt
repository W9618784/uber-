package com.example.pracainz.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pracainz.R
import com.example.pracainz.activities.BookTaxiActivity
import com.example.pracainz.activities.MapDetails
import com.example.pracainz.models.User

class DriverListAdapter(private val context: Context, private val driverList: List<User>) :
    RecyclerView.Adapter<DriverListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameTextView: TextView = itemView.findViewById(R.id.usernameTextView)
        val phoneTextView: TextView = itemView.findViewById(R.id.phoneTextView)
        val locationTextView: TextView = itemView.findViewById(R.id.locationTextView)
        val btn:Button=itemView.findViewById(R.id.btn)
        val btn_book:Button=itemView.findViewById(R.id.btn_book)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_driver, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = driverList[position]

        // Populate the views with user data
        holder.usernameTextView.text = user.username
        holder.btn.setOnClickListener {
            val intent = Intent(context, MapDetails::class.java)
            intent.putExtra("LAT_EXTRA", user.lat)
            intent.putExtra("LNG_EXTRA", user.lng)
            context.startActivity(intent)
        }
        holder.btn_book.setOnClickListener {
            val intent = Intent(context, BookTaxiActivity::class.java)
            intent.putExtra("d_name", user.username)
            intent.putExtra("phone_no", user.phone)
            intent.putExtra("driver_id", user.uid)
            context.startActivity(intent)
        }
        holder.phoneTextView.text = user.phone
        holder.locationTextView.text = "Lat: ${user.lat}, Lng: ${user.lng}"
    }

    override fun getItemCount(): Int {
        return driverList.size
    }

}

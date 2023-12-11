package com.example.pracainz.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pracainz.R
import com.example.pracainz.models.Booking

class BookingAdapter(private val bookingList: List<Booking>) : RecyclerView.Adapter<BookingAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taxiNumberTextView: TextView = itemView.findViewById(R.id.tvTaxiNumber)
        val driverNameTextView: TextView = itemView.findViewById(R.id.tvDriverName)
        val addressTextView: TextView = itemView.findViewById(R.id.tvAddress)
        val phoneNumberTextView: TextView = itemView.findViewById(R.id.tvPhoneNumber)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_booking, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val booking = bookingList[position]

        // Bind data to views
        holder.taxiNumberTextView.text = booking.taxiNumber
        holder.driverNameTextView.text = booking.driverName
        holder.addressTextView.text = booking.address
        holder.phoneNumberTextView.text = booking.phoneNumber
    }

    override fun getItemCount(): Int {
        return bookingList.size
    }
}

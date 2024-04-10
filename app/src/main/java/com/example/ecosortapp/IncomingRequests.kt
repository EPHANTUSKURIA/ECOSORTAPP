package com.example.ecosortapp.requests

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecosortapp.databinding.ActivityRequestsBinding
import com.example.ecosortapp.requests.adapter.RequestsAdapter
import com.example.ecosortapp.requests.model.RequestData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class IncomingRequests : AppCompatActivity(), RequestsAdapter.OnRequestClickListener {

    private lateinit var binding: ActivityRequestsBinding
    private lateinit var adapter: RequestsAdapter
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private val requestArrayList = mutableListOf<RequestData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()

        adapter = RequestsAdapter(requestArrayList, this@IncomingRequests)
        binding.requestRecycler.apply {
            layoutManager = LinearLayoutManager(this@IncomingRequests)
            adapter = this@IncomingRequests.adapter
        }

        getIncomingRequests()
    }

    override fun onRequestClick(request: RequestData, position: Int) {
        // Handle click on request item
        // For example, show a toast
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
    }

    private fun getIncomingRequests() {
        database = FirebaseDatabase.getInstance().reference
        val currentTimeMillis = System.currentTimeMillis()

        database.child("Requests")
            .orderByChild("time")
            .startAt(currentTimeMillis.toDouble())
            .get()
            .addOnSuccessListener { dataSnapshot ->
                for (itemSnapshot in dataSnapshot.children) {
                    val id = itemSnapshot.child("id").getValue(String::class.java)
                    val imageUrl = itemSnapshot.child("imageUrl").getValue(String::class.java)
                    val client = itemSnapshot.child("client").getValue(String::class.java)
                    val requestMail = itemSnapshot.child("requestMail").getValue(String::class.java)
                    val weight = itemSnapshot.child("weight").getValue(String::class.java)
                    val timeString = itemSnapshot.child("time").getValue(String::class.java)
                    val tvSelectDate = itemSnapshot.child("tvSelectDate").getValue(String::class.java)
                    val uid = itemSnapshot.child("uid").getValue(String::class.java)

                    // Parsing timeString to milliseconds since epoch
                    val requestTimeMillis = parseTimeStringToMillis(timeString)

                    // Check if request time is in the future
                    if (requestTimeMillis > currentTimeMillis) {
                        // Request time is in the future, add it to the list
                        if (id != null && imageUrl != null && client != null && requestMail != null && uid != null && weight != null && timeString != null && tvSelectDate != null) {
                            val request = RequestData(id, uid, client, requestMail, weight, timeString, tvSelectDate, imageUrl)
                            requestArrayList.add(request)
                        }
                    }
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Handle any errors
                Toast.makeText(this@IncomingRequests, "Failed to retrieve data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun parseTimeStringToMillis(timeString: String?): Long {
        if (timeString == null || timeString.isEmpty()) {
            return 0
        }
        val parts = timeString.split(":")
        if (parts.size != 2) {
            throw IllegalArgumentException("Invalid time format: $timeString")
        }
        val hours = parts[0].toLong()
        val minutes = parts[1].toLong()
        return hours * 60 * 60 * 1000 + minutes * 60 * 1000
    }

}

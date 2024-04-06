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
            .orderByChild("dateTime") // Assuming you have a field named "dateTime" representing the combined date and time
            .startAt(currentTimeMillis.toDouble()) // Filter by current date and time
            .get()
            .addOnSuccessListener { dataSnapshot ->
                for (itemSnapshot in dataSnapshot.children) {
                    val id = itemSnapshot.child("id").getValue(String::class.java)
                    val imageUrl = itemSnapshot.child("imageUrl").getValue(String::class.java)
                    val client = itemSnapshot.child("client").getValue(String::class.java)
                    val requestMail = itemSnapshot.child("requestMail").getValue(String::class.java)
                    val weight = itemSnapshot.child("weight").getValue(String::class.java)
                    val time = itemSnapshot.child("time").getValue(String::class.java) // Assuming dateTime is stored as a Unix timestamp
                    val tvSelectDate = itemSnapshot.child("tvSelectDate").getValue(String::class.java)
                    val uid = itemSnapshot.child("uid").getValue(String::class.java)

                    // Add logic to filter based on date and time if needed

                    if (id != null && imageUrl != null && client != null && requestMail != null && uid != null && weight != null && time != null && tvSelectDate != null) {
                        val request = RequestData(id, uid, client, requestMail, weight, time, tvSelectDate, imageUrl)
                        requestArrayList.add(request)
                    }
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Handle any errors
                Toast.makeText(this@IncomingRequests, "Failed to retrieve data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

}

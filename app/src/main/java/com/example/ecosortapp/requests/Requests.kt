package com.example.ecosortapp.requests

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecosortapp.databinding.ActivityRequestsBinding
import com.example.ecosortapp.requests.adapter.RequestsAdapter
import com.example.ecosortapp.requests.model.RequestData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class Requests : AppCompatActivity(), RequestsAdapter.OnRequestClickListener {

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

        adapter = RequestsAdapter(requestArrayList, this@Requests)
        binding.requestRecycler.apply {
            layoutManager = LinearLayoutManager(this@Requests)
            adapter = this@Requests.adapter
        }

        getRequests()
    }

    override fun onRequestClick(request: RequestData, position: Int) {
        // Handle click on request item
        // For example, show a toast
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
    }

    private fun getRequests() {
        database = Firebase.database.reference
        database.child("Requests").get()
            .addOnSuccessListener { dataSnapshot ->
                for (itemSnapshot in dataSnapshot.children) {
                    val id = itemSnapshot.child("id").getValue(String::class.java)
                    val imageUrl = itemSnapshot.child("imageUrl").getValue(String::class.java)
                    val client = itemSnapshot.child("client").getValue(String::class.java)
                    val requestMail = itemSnapshot.child("requestMail").getValue(String::class.java)
                    val weight = itemSnapshot.child("weight").getValue(String::class.java)
                    val time = itemSnapshot.child("time").getValue(String::class.java)
                    val tvSelectDate = itemSnapshot.child("tvSelectDate").getValue(String::class.java)
                    val uid = itemSnapshot.child("uid").getValue(String::class.java)

                    // Check if 'time' field is not null before using it
                    val parsedTime = if (time != null) time else "N/A"

                    if (id != null && imageUrl != null && client != null && requestMail != null && uid != null && weight != null && tvSelectDate != null) {
                        val request = RequestData(id, uid, client, requestMail, weight, parsedTime, tvSelectDate, imageUrl)
                        requestArrayList.add(request)
                    }
                }
                adapter.notifyDataSetChanged()
            }
    }
}

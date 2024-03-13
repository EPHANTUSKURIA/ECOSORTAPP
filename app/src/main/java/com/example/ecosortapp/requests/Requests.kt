package com.example.ecosortapp.requests

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecosortapp.R
import com.example.ecosortapp.adapter.InfoAdapter
import com.example.ecosortapp.databinding.ActivityRequestsBinding
import com.example.ecosortapp.info.model.BlogData
import com.example.ecosortapp.requests.adapter.RequestsAdapter
import com.example.ecosortapp.requests.model.RequestData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class Requests : AppCompatActivity(), RequestsAdapter.OnRequestClickListener {

    private lateinit var binding : ActivityRequestsBinding

    private lateinit var adapter: RequestsAdapter
    private lateinit var recyclerView: RecyclerView
    private var requestArrayList = mutableListOf<RequestData>()

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRequestsBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requests)

        auth  =  FirebaseAuth.getInstance()

        // Initialize RecyclerView and Adapter
        recyclerView = binding.requestRecycler
        adapter = RequestsAdapter(requestArrayList, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        getRequests()


    }

    override fun onRequestClick(request: RequestData, position: Int) {
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getRequests() {
        database = Firebase.database.reference
        database.child("Requests").get()
            .addOnSuccessListener { dataSnapshot ->
                for (jobSnapshot in dataSnapshot.children) {
                    val id = jobSnapshot.child("id").getValue(String::class.java)
                    val imageUrl = jobSnapshot.child("imageUrl").getValue(String::class.java)
                    val client = jobSnapshot.child("client").getValue(String::class.java)
                    val requestMail = jobSnapshot.child("requestMail").getValue(String::class.java)
                    val weight = jobSnapshot.child("weight").getValue(String::class.java)
                    val time = jobSnapshot.child("time").getValue(String::class.java)
                    val tvSelectDate = jobSnapshot.child("tvSelectDate").getValue(String::class.java)
                    val uid = jobSnapshot.child("uid").getValue(String::class.java)

                    if (id != null && imageUrl != null && client != null && requestMail != null && uid != null && weight != null && time != null && tvSelectDate != null) {
                        val request = RequestData(id, uid, imageUrl, client, requestMail, weight, time, tvSelectDate)
                        requestArrayList.add(request)
                    }
                }
                // Update the adapter's data and notify the RecyclerView
                adapter.notifyDataSetChanged()
                println("Array list is: ${requestArrayList}")
            }
    }

}
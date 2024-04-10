package com.example.ecosortapp.admin.adminfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecosortapp.R
import com.example.ecosortapp.databinding.FragmentAdminHomeBinding
import com.example.ecosortapp.requests.adapter.RequestsAdapter
import com.example.ecosortapp.requests.model.RequestData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database


class AdminHomeFragment : Fragment(),RequestsAdapter.OnRequestClickListener  {
    private lateinit var binding : FragmentAdminHomeBinding

    private lateinit var adapter: RequestsAdapter
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private val requestArrayList = mutableListOf<RequestData>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAdminHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        adapter = RequestsAdapter(requestArrayList, this@AdminHomeFragment)
        binding.requestRecycler.apply {
            layoutManager = LinearLayoutManager(this@AdminHomeFragment.requireContext())
            adapter = this@AdminHomeFragment.adapter
        }

        getRequests()


    }

    override fun onRequestClick(request: RequestData, position: Int) {
        // Handle click on request item
        // For example, show a toast
        Toast.makeText(requireActivity(), "Clicked", Toast.LENGTH_SHORT).show()
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

                    if (id != null && imageUrl != null && client != null && requestMail != null && uid != null && weight != null && time != null && tvSelectDate != null) {
                        val request = RequestData(id, uid, client, requestMail, weight, tvTime = null, tvSelectDate, imageUrl)
                        requestArrayList.add(request)
                    }
                }
                adapter.notifyDataSetChanged()
            }
    }

}
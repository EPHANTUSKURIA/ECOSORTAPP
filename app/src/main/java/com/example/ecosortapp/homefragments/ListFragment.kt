package com.example.ecosortapp.homefragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecosortapp.Home
import com.example.ecosortapp.R
import com.example.ecosortapp.adapter.CollectorsAdapter
import com.example.ecosortapp.admin.model.CollectorData
import com.example.ecosortapp.databinding.FragmentListBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database


class ListFragment : Fragment(), CollectorsAdapter.OnCollectorClickListener {

    private lateinit var binding: FragmentListBinding
    private lateinit var adapter: CollectorsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var collectorArrayList: MutableList<CollectorData>

    private lateinit var database: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        // Initialize collectorArrayList
        collectorArrayList = mutableListOf()

        getCollectors()

        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.collectorsRecyclerView)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = CollectorsAdapter(collectorArrayList, this)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onCollectorClick(collector: CollectorData, position: Int) {
        val intent = Intent(requireActivity(), Home::class.java)
        // Pass an extra to indicate that we want to navigate to the ScheduleFragment directly
        intent.putExtra("navigateToScheduleFragment", true)
        intent.putExtra("collectorId", collector.uid)
        startActivity(intent)
    }

    private fun getCollectors() {
        database = Firebase.database.reference
        database.child("Collectors").get()
            .addOnSuccessListener { dataSnapshot ->
                for (collectorSnapshot in dataSnapshot.children) {
                    val collectorName =
                        collectorSnapshot.child("collectorName").getValue(String::class.java)
                    val collectorLocation =
                        collectorSnapshot.child("collectorLocation").getValue(String::class.java)
                    val uid = collectorSnapshot.child("uid").getValue(String::class.java)

                    if (collectorName != null && collectorLocation != null && uid != null) {
                        val collector = CollectorData(
                            uid, collectorName, collectorLocation
                        )
                        collectorArrayList.add(collector)
                    }
                }
                adapter.notifyDataSetChanged()

            }
    }


}
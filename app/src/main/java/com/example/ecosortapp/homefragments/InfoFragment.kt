package com.example.ecosortapp.homefragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecosortapp.R
import com.example.ecosortapp.adapter.InfoAdapter
import com.example.ecosortapp.databinding.FragmentInfoBinding
import com.example.ecosortapp.info.MoreInfo
import com.example.ecosortapp.info.Upload_Info
import com.example.ecosortapp.info.model.BlogData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database


class InfoFragment : Fragment(), InfoAdapter.OnInfoClickListener {

    private lateinit var binding: FragmentInfoBinding

    private lateinit var adapter: InfoAdapter
    private lateinit var recyclerView: RecyclerView
    private var blogArrayList = mutableListOf<BlogData>()

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.FAB.setOnClickListener {
            val intent = Intent(requireActivity(), Upload_Info::class.java)
            startActivity(intent)
        }

        getInfo()
        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.infoRecycler)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = InfoAdapter(blogArrayList, this)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onInfoClick(info: BlogData, position: Int) {
        val intent = Intent(requireActivity(), MoreInfo::class.java)
        intent.putExtra("image", info.imageUrl.toString())
        intent.putExtra("blogTitle", info.blogTitle)
        intent.putExtra("blogContent", info.blogContent)
        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getInfo() {
        database = Firebase.database.reference
        database.child("Blogs").get()
            .addOnSuccessListener { dataSnapshot ->
                for (jobSnapshot in dataSnapshot.children) {
                    val id = jobSnapshot.child("id").getValue(String::class.java)
                    val imageUrl = jobSnapshot.child("imageUrl").getValue(String::class.java)
                    val blogTitle = jobSnapshot.child("blogTitle").getValue(String::class.java)
                    val blogContent = jobSnapshot.child("blogContent").getValue(String::class.java)
                    val uid = jobSnapshot.child("uid").getValue(String::class.java)

                    if (id != null && imageUrl != null && blogTitle != null && blogContent != null && uid != null) {
                        val blog = BlogData(id, uid, imageUrl, blogTitle, blogContent)
                        blogArrayList.add(blog)
                    }
                }
                adapter.notifyDataSetChanged()

            }
    }

}
package com.example.ecosortapp.homefragments

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.util.Log
import android.content.ContentValues.TAG
import com.example.ecosortapp.auth.model.UserData
import com.example.ecosortapp.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference.child("Blogs")

        fetchAndDisplayUserBlogPostsCount()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        // Fetch and display user name
        fetchAndDisplayUserName()


        binding.profilePicture.setOnClickListener{
            val intent = Intent(requireActivity(), Profile::class.java)
            startActivity(intent)
        }

        binding.redeem.setOnClickListener {
            val points = 1000
            if (points == 1000) {
                Toast.makeText(requireActivity(), "Please attain the limit of 1000 points to redeem and earn", Toast.LENGTH_LONG).show()
            } else Toast.makeText(requireActivity(), "Eligible to Redeem", Toast.LENGTH_LONG).show()

        }
    }

    private fun fetchAndDisplayUserBlogPostsCount() {
        val currentUserId = firebaseAuth.currentUser?.uid

        if (currentUserId != null) {
            databaseReference.orderByChild("uid").equalTo(currentUserId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val userBlogPostsCount = dataSnapshot.childrenCount.toInt()
                        val points = userBlogPostsCount * 10

                        binding.points.text = points.toString()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle database error
                    }
                })
        }
    }

    private fun fetchAndDisplayUserName() {
        val currentUser = firebaseAuth.currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            val userRef =
                FirebaseDatabase.getInstance().getReference("registeredUser").child(userId)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        Log.e(TAG, "Exists")
                        val userData = snapshot.getValue(UserData::class.java)
                        Log.e(TAG, userData.toString())
                        if (userData != null) {
                            val userName = userData.name ?: "Unknown"
                            Log.e(TAG, "HomeFragmentUser name retrieved: %s")
                            binding.userName.text = userName
                        } else {
                            Log.e(TAG, "user is null")
                        }
                    } else {
                        Log.e(TAG, "Data snapshot does not exist")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                    Log.e(TAG, "Failed to fetch")
                    Toast.makeText(
                        requireContext(),
                        "Failed to fetch user data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } else {
            Log.e(TAG, "Current user id is null")
        }
    }
}

package com.example.ecosortapp.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.ecosortapp.auth.Login
import com.example.ecosortapp.databinding.ActivityProfileBinding
import com.example.ecosortapp.auth.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Profile : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout for this activity
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth and Database Reference
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("registeredUser")
        getData()

        // Set up logout button click listener
        binding.btnLogout.setOnClickListener {
            logout()
        }
    }
    private fun getData() {
        val uid = auth.currentUser?.uid
        if (uid.isNullOrEmpty()) {
            // User not authenticated
            return
        }

        // Get a reference to the user data in the database
        val ref = database.child(uid)

        // Retrieve user data from the database
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // User data found, parse it and display in the UI
                    val userData = snapshot.getValue(UserData::class.java)
                    userData?.let {
                        // Update UI with user data
                        binding.userName.text = it.name
                        binding.userEmail.text = it.email
                        binding.userPhone.text = it.phone
                    }
                } else {
                    // User data not found
                    Toast.makeText(this@Profile, "User data not found", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Error fetching user data
                Log.e(TAG, "Failed to fetch user data: ${error.message}")
                Toast.makeText(this@Profile, "Failed to fetch user data", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun logout() {
        auth.signOut()
        // Redirect to LoginActivity
        val intent = Intent(this, Login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    companion object {
        private const val TAG = "ProfileActivity"
    }


}







package com.example.ecosortapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ecosortapp.auth.Login
import com.example.ecosortapp.auth.model.UserData
import com.example.ecosortapp.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Profile : AppCompatActivity() {
    private lateinit var binding : ActivityProfileBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityProfileBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("registeredUser")
        getData()

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
                        binding.etName.text = it.name
                        binding.etEmail.text = it.email
                        binding.etPhoneNumber.text = it.phone
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
        this.finish()
    }

    companion object {
        private const val TAG = "ProfileFragment"
    }
}
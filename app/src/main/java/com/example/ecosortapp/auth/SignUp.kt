package com.example.ecosortapp.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ecosortapp.Home
import com.example.ecosortapp.R
import com.example.ecosortapp.databinding.ActivityLoginBinding
import com.example.ecosortapp.databinding.ActivitySignUpBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        registerEvents()

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }
    }

    private var email = ""
    private var user = ""
    private var phone = ""
    private fun registerEvents() {
        binding.btnRegister.setOnClickListener {
            email = binding.userEmail.text.toString().trim()
            user = binding.userName.text.toString().trim()
            phone = binding.userPhone.text.toString().trim()
            val pass = binding.passEt.text.toString().trim()
            val verifyPass = binding.verifyPassEt.text.toString().trim()

            if (email.isNotEmpty() && pass.isNotEmpty() && verifyPass.isNotEmpty() && user.isNotEmpty() && phone.isNotEmpty()) {
                if (pass == verifyPass) {
                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            createUserDetails(timeStamp)
                            binding.userName.text?.clear()
                            binding.userPhone.text?.clear()
                            binding.passEt.text?.clear()
                            binding.verifyPassEt.text?.clear()
                            binding.userEmail.text?.clear()
                            Toast.makeText(
                                this,
                                "Registration successful",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(this, Home::class.java))
                            finish()


                        } else {
                            Toast.makeText(this, it.exception!!.message, Toast.LENGTH_SHORT)
                                .show()
                        }

                    }
                } else {
                    Toast.makeText(
                        this,
                        "Passwords Don't Match",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {
                Toast.makeText(
                    this,
                    "Empty Fields Not Allowed",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    val timeStamp = System.currentTimeMillis()
    private fun createUserDetails(timeStamp: Long) {
        val uid = FirebaseAuth.getInstance().uid
        val hashMap: HashMap<String, Any> = HashMap()

        hashMap["uid"] = "$uid"
        hashMap["name"] = "$user"
        hashMap["email"] = "$email"
        hashMap["phone"] = "$phone"

        val ref = FirebaseDatabase.getInstance().getReference("registeredUser")
        ref.child("$uid")
            .setValue(hashMap)
            .addOnSuccessListener {
                Toast.makeText(
                    this,
                    "Registered Successfully",
                    Toast.LENGTH_SHORT
                ).show()

            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Registration Failed due to ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()

            }
    }


}
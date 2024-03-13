package com.example.ecosortapp.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ecosortapp.Home
import com.example.ecosortapp.R
import com.example.ecosortapp.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.btnLogin.setOnClickListener {
            registerEvents()
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }

    }

    private fun registerEvents() {
        auth = FirebaseAuth.getInstance()
        binding.btnLogin.setOnClickListener {
            val email = binding.emailEt.text.toString().trim()
            val pass = binding.passEt.text.toString().trim()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(
                    OnCompleteListener {
                        if (it.isSuccessful) {
                            binding.emailEt.text?.clear()
                            binding.passEt.text?.clear()
                            Toast.makeText(
                                this,
                                "Logged In successfully",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {
                            Toast.makeText(this, it.exception!!.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                        if (email.isNotEmpty() && pass.isNotEmpty()) {
                            startActivity(Intent(this, Home::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT)
                                .show()
                        }
                    })
            } else {
                Toast.makeText(
                    this,
                    "Empty Fields Not Allowed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
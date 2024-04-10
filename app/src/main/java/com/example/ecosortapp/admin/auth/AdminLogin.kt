package com.example.ecosortapp.admin.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ecosortapp.Home
import com.example.ecosortapp.R
import com.example.ecosortapp.admin.home.AdmiHome
import com.example.ecosortapp.auth.Login
import com.example.ecosortapp.auth.SignUp
import com.example.ecosortapp.databinding.ActivityAdminLoginBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class AdminLogin : AppCompatActivity() {
    private lateinit var binding : ActivityAdminLoginBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAdminLoginBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.btnLoginAsAdmin.setOnClickListener {
            registerEvents()
        }


        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }


        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }

    }

    private fun registerEvents() {
        auth = FirebaseAuth.getInstance()
        binding.btnLoginAsAdmin.setOnClickListener {
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
                            startActivity(Intent(this, AdmiHome::class.java))
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
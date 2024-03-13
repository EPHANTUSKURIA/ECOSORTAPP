package com.example.ecosortapp.info

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.ecosortapp.R
import com.example.ecosortapp.databinding.ActivityMoreInfoBinding

class MoreInfo : AppCompatActivity() {
    private lateinit var binding: ActivityMoreInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMoreInfoBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        Glide.with(this)
            .load(intent.getStringExtra("image"))
            .into(binding.selectImage)
        binding.apply {
            intent
            blogTitle.text = intent.getStringExtra("blogTitle")
            blogContent.text = intent.getStringExtra("blogContent")

        }

    }
}
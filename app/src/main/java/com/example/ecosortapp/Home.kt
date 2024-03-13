package com.example.ecosortapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.ecosortapp.databinding.ActivityHomeBinding
import com.example.ecosortapp.homefragments.HomeFragment
import com.example.ecosortapp.homefragments.InfoFragment
import com.example.ecosortapp.homefragments.RequestFragment

class Home : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding  = ActivityHomeBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        replaceFragment(HomeFragment())
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.icShop -> replaceFragment(HomeFragment())
                R.id.icJob -> replaceFragment(RequestFragment())
                R.id.icCategories -> replaceFragment(InfoFragment())

                else -> {
                }
            }
            true
        }
        if (resources.getColor(R.color.background_tint_dark) == resources.getColor(R.color.background_tint_dark)) {
            binding.bottomNavigationView.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.background_tint_dark
                )
            )
        } else {
            binding.bottomNavigationView.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.background_tint_light
                )
            )


        }


    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}
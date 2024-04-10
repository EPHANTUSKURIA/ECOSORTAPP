package com.example.ecosortapp.admin.home

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.ecosortapp.R
import com.example.ecosortapp.admin.adminfragments.AddAdmainFragment
import com.example.ecosortapp.admin.adminfragments.AdminHomeFragment
import com.example.ecosortapp.databinding.ActivityAdmiHomeBinding

class AdmiHome : AppCompatActivity() {
    private lateinit var binding : ActivityAdmiHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAdmiHomeBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        replaceFragment(AdminHomeFragment())

        binding.docBottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.icSchedule -> replaceFragment(AdminHomeFragment())
                R.id.icRegister -> replaceFragment(AddAdmainFragment())

                else -> {
                }
            }
            true
        }

    }
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.doctorFrameLayout, fragment)
        fragmentTransaction.commit()
    }
}
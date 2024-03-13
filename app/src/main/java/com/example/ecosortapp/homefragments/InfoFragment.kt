package com.example.afyamkononi.fragments

import android.app.Notification
import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecosortapp.R
import com.example.ecosortapp.databinding.FragmentInfoBinding
import com.google.firebase.auth.FirebaseAuth


class InfoFragmentFragment : Fragment() {
    private lateinit var binding: FragmentInfoBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private var infoUri: Uri? = null
    private val TAG = "REQUEST ADD TAG"
    lateinit var tvDate: TextView
    lateinit var btnshowdatepicker: Button

    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        infoUri = it
        binding.selectImage.setImageURI(infoUri)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataInitialize()
        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.notificationsRecycler)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = NotificationAdapter(notificationArrayList, this)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onNotificationClick(notification: NotificationData, position: Int) {
        Toast.makeText(requireActivity(), "Clicked", Toast.LENGTH_LONG).show()
    }

    private fun dataInitialize(){
        notificationArrayList = arrayListOf(
            NotificationData("BMI Updated", "Your new Bmi is 23.4"),
            NotificationData("New Scan", "New Injury scan"),
            NotificationData("New Meeting", "You have a new meeting"),
            NotificationData("Past Meeting", "Just attended this meeting"),
            NotificationData("BMI Updated", "Your new Bmi is 23.4"),
            NotificationData("New Scan", "New Injury scan"),
            NotificationData("New Meeting", "You have a new meeting"),
            NotificationData("Past Meeting", "Just attended this meeting")
        )
    }


}
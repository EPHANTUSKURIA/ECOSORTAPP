package com.example.ecosortapp.admin.adminfragments

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.example.ecosortapp.databinding.FragmentAddAdmainBinding
import com.google.firebase.auth.FirebaseAuth


class AddAdmainFragment : Fragment() {
    private lateinit var binding: FragmentAddAdmainBinding


    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddAdmainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.btnAdd.setOnClickListener {
            validateData()
        }
    }

    private var collectorName = ""
    private var collectorLocation = ""
    private fun validateData() {
        collectorName = binding.collectorName.text.toString().trim()
        collectorLocation = binding.collectorLocation.text.toString().trim()


        if (collectorName.isEmpty()) {
            Toast.makeText(requireActivity(), "Enter  Name ", Toast.LENGTH_SHORT)
                .show()
        } else if (collectorLocation.isEmpty()) {
            Toast.makeText(requireActivity(), "Enter  Location", Toast.LENGTH_SHORT).show()
        } else {
            uploadDoctorInfoToDb()
            binding.collectorName.text?.clear()
            binding.collectorLocation.text?.clear()

        }

    }

    private fun uploadDoctorInfoToDb() {
        val timeStamp = System.currentTimeMillis()

        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Uploading data")
        val uid = FirebaseAuth.getInstance().uid
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["id"] = "$timeStamp"
        hashMap["uid"] = "$uid"
        hashMap["collectorName"] = "$collectorName"
        hashMap["collectorLocation"] = "$collectorLocation"


        val ref = FirebaseDatabase.getInstance().getReference("Collectors")
        ref.child("$timeStamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(
                    requireActivity(),
                    "Uploaded",
                    Toast.LENGTH_SHORT
                ).show()

            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(
                    requireActivity(),
                    "Uploading Event Failed due to ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()

            }
    }


}
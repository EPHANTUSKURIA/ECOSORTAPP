package com.example.ecosortapp.homefragments

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.ecosortapp.databinding.FragmentRequestBinding
import com.example.ecosortapp.requests.IncomingRequests
import com.example.ecosortapp.requests.PassedRequests
import com.example.ecosortapp.requests.Requests
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class RequestFragment : Fragment() {
    private lateinit var binding: FragmentRequestBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private var requestUri: Uri? = null
    private val TAG = "REQUEST_ADD_TAG"
    private lateinit var tvDate: TextView
    private lateinit var btnShowDatePicker: Button
    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        requestUri = it
        binding.selectImage.setImageURI(requestUri)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private val calendar = Calendar.getInstance()

    private var client = ""
    private var requestMail = ""
    private var weight = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvDate = binding.tvSelectDate
        btnShowDatePicker = binding.btnshowDatePicker

        btnShowDatePicker.setOnClickListener {
            showDatePicker()
        }

        binding.allRequests.setOnClickListener {
            startActivity(Intent(requireActivity(), Requests::class.java))
        }

        binding.incomingCollection.setOnClickListener {
            startActivity(Intent(requireActivity(), IncomingRequests::class.java))
        }

        binding.passedCollection.setOnClickListener {
            startActivity(Intent(requireActivity(), PassedRequests::class.java))
        }

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(requireActivity())
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.selectImage.setOnClickListener {
            selectImage.launch("image/*")
        }

        binding.btnSaveEvent.setOnClickListener {
            validateData()
        }

        val btnPickTime = binding.btnPickTime
        val tvTime = binding.tvTime

        btnPickTime.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.HOUR_OF_DAY, minute)
                tvTime.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(
                requireActivity(),
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }
    }

    private fun validateData() {
        Log.d(TAG, "Validating Data")
        client = binding.client.text.toString().trim()
        requestMail = binding.requestMail.text.toString().trim()
        weight = binding.weight.text.toString().trim()
        val tvTime = binding.tvTime.text.toString().trim()
        val tvSelectDate = binding.tvSelectDate.text.toString().trim()

        if (client.isEmpty()) {
            Toast.makeText(requireActivity(), "Enter Name", Toast.LENGTH_SHORT).show()
        } else if (requestMail.isEmpty()) {
            Toast.makeText(requireActivity(), "Enter Email", Toast.LENGTH_SHORT).show()
        } else if (weight.isEmpty()) {
            Toast.makeText(requireActivity(), "Weight in KGs", Toast.LENGTH_SHORT).show()
        } else if (tvTime.isEmpty()) {
            Toast.makeText(requireActivity(), "Set the time", Toast.LENGTH_SHORT).show()
        } else if (tvSelectDate.isEmpty()) {
            Toast.makeText(requireActivity(), "Set the day", Toast.LENGTH_SHORT).show()
        } else {
            uploadRequestToStorage(tvTime, tvSelectDate)
            binding.client.text?.clear()
            binding.requestMail.text?.clear()
            binding.weight.text?.clear()
        }
    }

    private fun uploadRequestToStorage(iso8601Time: String?, iso8601Date: String?) {
        Log.d(TAG, "Requesting")
        progressDialog.setMessage("Request Collection")
        progressDialog.show()

        val timeStamp = System.currentTimeMillis()
        val filePathAndName = "request/$timeStamp"
        val storageReference = FirebaseStorage.getInstance().getReference(filePathAndName)
        storageReference.putFile(requestUri!!)
            .addOnSuccessListener { taskSnapShot ->
                val uriask: Task<Uri> = taskSnapShot.storage.downloadUrl
                while (!uriask.isSuccessful);
                val uploadedImageUrl = "${uriask.result}"

                requestCollectionInfoToDb(uploadedImageUrl, timeStamp, iso8601Time, iso8601Date)

                progressDialog.dismiss()
            }
    }

    private fun requestCollectionInfoToDb(
        uploadedImageUrl: String,
        timeStamp: Long,
        iso8601Time: String?,
        iso8601Date: String?
    ) {
        val progressDialog = ProgressDialog(requireActivity())
        progressDialog.setMessage("Uploading data")
        val uid = FirebaseAuth.getInstance().uid
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["id"] = "$timeStamp"
        hashMap["uid"] = "$uid"
        hashMap["client"] = client
        hashMap["requestMail"] = requestMail
        hashMap["weight"] = weight
        hashMap["time"] = iso8601Time ?:""
        hashMap["tvSelectDate"] = iso8601Date ?:""
        hashMap["clientLocation"] = requestMail
        hashMap["requestDescription"] = iso8601Date ?:""
        hashMap["imageUrl"] = uploadedImageUrl

        val ref = FirebaseDatabase.getInstance().getReference("Requests")
        ref.child("$timeStamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(
                    requireActivity(),
                    "Uploaded",
                    Toast.LENGTH_SHORT
                ).show()
                requestUri = null
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(
                    requireActivity(),
                    "Requesting collection failed ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()

            }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            requireActivity(), { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate: String = dateFormat.format(selectedDate.time)
                tvDate.text = "Selected Date is  : $formattedDate"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}







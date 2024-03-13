package com.example.ecosortapp.homefragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ecosortapp.R
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.example.ecosortapp.databinding.FragmentRequestBinding
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
    private val TAG = "REQUEST ADD TAG"

    lateinit var tvDate: TextView
    lateinit var btnshowdatepicker: Button

    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        requestUri = it
        binding.selectImage.setImageURI(requestUri)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private val calendar = Calendar.getInstance()


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
        btnshowdatepicker = binding.btnshowDatePicker

        btnshowdatepicker.setOnClickListener {
            showDatePicker()
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
            val cal = java.util.Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
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
    private var personMeet = ""
    private var requestMail = ""
    private var weight = ""
    private var tvTime = ""
    private var tvSelectDate = ""

    private fun validateData() {
        Log.d(TAG, "Validating Data")
        personMeet = binding.personMeet.text.toString().trim()
        requestMail = binding.requestMail.text.toString().trim()
        weight = binding.weight.text.toString().trim()
        tvTime = binding.tvTime.text.toString().trim()
        tvSelectDate = binding.tvSelectDate.text.toString().trim()

        if (personMeet.isEmpty()) {
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
            uploadrequestToStorage()
            binding.personMeet.text?.clear()
            binding.requestMail.text?.clear()
            binding.weight.text?.clear()
        }

    }

    private fun uploadrequestToStorage() {
        TODO("Not yet implemented")
    }

    private fun requestCollectionToStorage() {
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

                requestColectionInfoToDb(uploadedImageUrl, timeStamp)

                progressDialog.dismiss()
            }
    }

    private fun requestColectionInfoToDb(uploadedImageUrl: String, timeStamp: Long) {

    }

    private fun uploadJobInfoToDb(uploadedImageUrl: String, timeStamp: Long) {
        val progressDialog = ProgressDialog(requireActivity())
        progressDialog.setMessage("Uploading data")
        val uid = FirebaseAuth.getInstance().uid
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["id"] = "$timeStamp"
        hashMap["uid"] = "$uid"
        hashMap["name"] = "$personMeet"
        hashMap["clientLocation"] = "$requestMail"
        hashMap["weight"] = "$weight"
        hashMap["time"] = "$tvTime"
        hashMap["requestDescription"] = "$tvSelectDate"
        hashMap["imageUrl"] = "$uploadedImageUrl"

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
            requireActivity(), { DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate: String = dateFormat.format(selectedDate.time)
                tvDate.text = "Selected Date is  : " + formattedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

}
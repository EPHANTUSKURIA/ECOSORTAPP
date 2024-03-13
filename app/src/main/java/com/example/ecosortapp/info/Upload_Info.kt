package com.example.ecosortapp.info

import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase
import com.example.ecosortapp.R
import com.example.ecosortapp.databinding.ActivityUploadInfoBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage

class Upload_Info : AppCompatActivity() {
    private lateinit var binding: ActivityUploadInfoBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private var blogUri: Uri? = null
    private val TAG = "Blog TAG"

    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        blogUri = it
        binding.selectImage.setImageURI(blogUri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUploadInfoBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.selectImage.setOnClickListener {
            selectImage.launch("image/*")
        }

        binding.btnPost.setOnClickListener {
            validateData()
        }

    }

    private var blogTitle = ""
    private var blogContent = ""

    private fun validateData() {
        Log.d(TAG, "Validating Data")
        blogTitle = binding.blogTitle.text.toString().trim()
        blogContent = binding.blogContent.text.toString().trim()


        if (blogTitle.isEmpty()) {
            Toast.makeText(this, "Enter Job Title", Toast.LENGTH_SHORT).show()
        } else if (blogContent.isEmpty()) {
            Toast.makeText(this, "Enter Job Location", Toast.LENGTH_SHORT).show()
        } else {
            uploadJobToStorage()
            binding.blogTitle.text?.clear()
            binding.blogContent.text?.clear()
        }

    }

    private fun uploadJobToStorage() {
        Log.d(TAG, "Uploading")
        progressDialog.setMessage("Uploading Job")
        progressDialog.show()

        val timeStamp = System.currentTimeMillis()
        val filePathAndName = "blog/$timeStamp"
        val storageReference = FirebaseStorage.getInstance().getReference(filePathAndName)
        storageReference.putFile(blogUri!!)
            .addOnSuccessListener { taskSnapShot ->
                val uriask: Task<Uri> = taskSnapShot.storage.downloadUrl
                while (!uriask.isSuccessful);
                val uploadedImageUrl = "${uriask.result}"

                uploadJobInfoToDb(uploadedImageUrl, timeStamp)

                progressDialog.dismiss()
            }
    }

    private fun uploadJobInfoToDb(uploadedImageUrl: String, timeStamp: Long) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading data")
        val uid = FirebaseAuth.getInstance().uid
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["id"] = "$timeStamp"
        hashMap["uid"] = "$uid"
        hashMap["blogTitle"] = "$blogTitle"
        hashMap["blogContent"] = "$blogContent"
        hashMap["imageUrl"] = "$uploadedImageUrl"

        val ref = FirebaseDatabase.getInstance().getReference("Blogs")
        ref.child("$timeStamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "Uploaded",
                    Toast.LENGTH_SHORT
                ).show()
                blogUri = null
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "Uploading Job Failed due to ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()

            }


    }
}
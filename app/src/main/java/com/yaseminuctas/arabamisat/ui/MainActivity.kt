package com.yaseminuctas.arabamisat.ui

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.yaseminuctas.arabamisat.model.Car
import com.yaseminuctas.arabamisat.ui.carsdetail.CarsDetailActivity
import com.yaseminuctas.arabamisat.R
import com.yaseminuctas.arabamisat.databinding.ActivityMainBinding
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var filePath: Uri? = null
    var carImagesUri: String = ""
    var carPlateNo: Int = 0
    var carDesc: String = ""
    private val PICK_IMAGE_REQUEST = 71
    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null
    var database: DatabaseReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataBinding()
        startFirebase()
        initClickListeners()
    }

    private fun initDataBinding() {
        binding =
            DataBindingUtil.setContentView(
                this,
                R.layout.activity_main
            )
    }

    fun startFirebase() {
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference
        database = FirebaseDatabase.getInstance().reference
    }

    fun initClickListeners() {
        binding.btnChoose.setOnClickListener {
            chooseImage()
        }

        binding.btnShowCars.setOnClickListener {
            startActivity(Intent(this@MainActivity, CarsDetailActivity::class.java))
        }

        binding.btnUpload.setOnClickListener {
            uploadImage()
            carPlateNo = binding.edtCarPlate.text.toString().toInt()
            carDesc = binding.edtDesc.text.toString()

        }

    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null
        ) {
            filePath = data.data
            try {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                binding.imgCar.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Fotoğraf Seç"), PICK_IMAGE_REQUEST)
    }

    private fun uploadImage() {
        if (filePath != null) {
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Yükleniyor...")
            progressDialog.show()
            val ref =
                storageReference!!
                    .child(
                        "images/"
                                + UUID.randomUUID().toString()
                    )
            ref.putFile(filePath!!)
                .addOnSuccessListener {
                    carImagesUri = ref.toString()
                    database?.child(carPlateNo.toString())!!.setValue(
                        Car(
                            carDesc,
                            carImagesUri
                        )
                    )
                    progressDialog.dismiss()
                    Toast.makeText(this@MainActivity, "Yüklendi", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    progressDialog.dismiss()
                    Toast.makeText(
                        this@MainActivity,
                        "Hata Oluştu. " + e.message,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                .addOnProgressListener { taskSnapshot ->
                    val progress =
                        100.0 * taskSnapshot.bytesTransferred / taskSnapshot
                            .totalByteCount
                    progressDialog.setMessage("Yükleniyor " + progress.toInt() + "%")
                }
        }
    }

}
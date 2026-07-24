package com.example.secretvault

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream

class VaultActivity : Activity() {

    private lateinit var ivSecretImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vault)

        ivSecretImage = findViewById(R.id.ivSecretImage)
        val btnAddImage = findViewById<Button>(R.id.btnAddImage)
        val btnViewGallery = findViewById<Button>(R.id.btnViewGallery)

        // 1. Add Image Button Click -> Opens System Picker for Multiple Photos
        btnAddImage.setOnClickListener {
            openImagePicker()
        }

        // 2. View Gallery Button Click -> Opens GalleryActivity
        btnViewGallery.setOnClickListener {
            val intent = Intent(this, GalleryActivity::class.java)
            startActivity(intent)
        }
    }

    // System file picker kholne ka function (Multiple Selection Enabled)
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        startActivityForResult(intent, 200)
    }

    // Selected photos ko receive karke internal storage mein save karne ka function
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 200 && resultCode == RESULT_OK) {
            // Agar user ne multiple photos select ki hain
            if (data?.clipData != null) {
                val count = data.clipData!!.itemCount
                for (i in 0 until count) {
                    val imageUri = data.clipData!!.getItemAt(i).uri
                    saveImageToInternalStorage(imageUri)
                }
                Toast.makeText(this, "$count photos saved to Vault!", Toast.LENGTH_SHORT).show()
            }
            // Agar user ne sirf 1 photo select ki hai
            else if (data?.data != null) {
                val imageUri = data.data!!
                saveImageToInternalStorage(imageUri)
                Toast.makeText(this, "1 photo saved to Vault!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Uri se photo copy karke app ke private folder mein save karne ka method
    private fun saveImageToInternalStorage(uri: Uri) {
        try {
            val inputStream = contentResolver.openInputStream(uri)
            val fileName = "secret_image_${System.currentTimeMillis()}_${(0..1000).random()}.jpg"
            val file = File(filesDir, fileName)
            val outputStream = FileOutputStream(file)

            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            // Optional: Turant preview ke liye pehli image dikha do
            ivSecretImage.setImageURI(Uri.fromFile(file))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
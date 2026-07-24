package com.example.secretvault

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast

class GalleryActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        // XML wale Grid ko Kotlin se link kiya
        val galleryGrid = findViewById<GridLayout>(R.id.galleryGrid)

        // 1. App ke secret folder (Internal Storage) tak pahunche
        val secretFolder = filesDir

        // 2. Us folder ke andar ki saari files (photos) nikal li
        val files = secretFolder.listFiles()

        // Check kiya ki folder khali toh nahi hai
        if (files == null || files.isEmpty()) {
            Toast.makeText(this, "Vault is empty. Add some photos!", Toast.LENGTH_SHORT).show()
            return
        }

        // 3. Har ek photo ke liye ek Naya ImageView banaya aur Grid mein daal diya
        for (file in files) {
            // Hum sirf unhi files ko uthayenge jinka naam humne 'secret_image_' rakha tha
            if (file.name.startsWith("secret_image_")) {

                val imageView = ImageView(this)

                // File se photo nikal kar ImageView mein lagayi
                imageView.setImageURI(Uri.fromFile(file))

                // Photo ki height, width aur margin (gap) set kiya
                val params = GridLayout.LayoutParams()
                params.width = 300  // Photo ki chaudayi (width)
                params.height = 300 // Photo ki lambayi (height)
                params.setMargins(10, 10, 10, 10)
                imageView.layoutParams = params

                // Photo ko box mein perfectly fit karne ke liye
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP

                // Final step: is photo ko hamare Grid mein add kar do!
                galleryGrid.addView(imageView)

                // --- CLICK LISTENER: Click karne par Full Screen open karna ---
                val currentPosition = galleryGrid.childCount - 1
                imageView.setOnClickListener {
                    val intent = Intent(imageView.context, FullScreenActivity::class.java)
                    intent.putExtra("PHOTO_POSITION", currentPosition)
                    startActivity(intent)
                }
            }
        }
    }
}
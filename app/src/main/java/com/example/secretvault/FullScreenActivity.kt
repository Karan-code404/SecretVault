package com.example.secretvault

import android.app.Activity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2

class FullScreenActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        // 1. Secret folder se wapas saari photos nikal li
        val files = filesDir.listFiles()?.filter { it.name.startsWith("secret_image_") } ?: emptyList()

        // 2. Apne Manager (Adapter) ko wo photos de di
        val adapter = PhotoPagerAdapter(files)
        viewPager.adapter = adapter

        // 3. Jis photo par click kiya hai, directly wahi photo open karne ka logic
        val clickedPosition = intent.getIntExtra("PHOTO_POSITION", 0)
        viewPager.setCurrentItem(clickedPosition, false)
    }
}
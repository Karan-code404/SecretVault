package com.example.secretvault

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.chrisbanes.photoview.PhotoView
import java.io.File

// Ye hamara Manager (Adapter) hai jo photos ko swipe list mein lagayega
class PhotoPagerAdapter(private val photoFiles: List<File>) : RecyclerView.Adapter<PhotoPagerAdapter.PhotoViewHolder>() {

    // Ye Box (ViewHolder) hai jo ek time par ek photo ko hold karega
    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoView: PhotoView = itemView.findViewById(R.id.photoView)
    }

    // 1. Naya Box banana (item_photo.xml design ko use karke)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return PhotoViewHolder(view)
    }

    // 2. Us Box mein actual photo set karna
    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val file = photoFiles[position]
        holder.photoView.setImageURI(Uri.fromFile(file))
    }

    // 3. Manager ko batana ki total kitni photos hain
    override fun getItemCount(): Int {
        return photoFiles.size
    }
}
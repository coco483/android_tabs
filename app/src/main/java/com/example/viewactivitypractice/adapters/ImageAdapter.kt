package com.example.viewactivitypractice.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.viewactivitypractice.R
import com.example.viewactivitypractice.datas.ImageData
import com.example.viewactivitypractice.datas.PostData
import com.example.viewactivitypractice.getBitmapFromPath

class ImageAdapter(private val imgList: ArrayList<ImageData>, private val onClick: (ImageData) -> Unit)
    : RecyclerView.Adapter<ImageAdapter.ImgViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImgViewHolder {
        var imgView =
            LayoutInflater.from(parent.context).inflate(R.layout.gallery_img_items, parent, false)
        return ImgViewHolder(imgView, onClick)
    }
    override fun onBindViewHolder(holder: ImgViewHolder, position: Int) {
        val currentItem = imgList[position]
        val bitImg = getBitmapFromPath(currentItem.imgPath)
        holder.img.setImageBitmap(bitImg)
        holder.itemView.setOnClickListener {onClick(currentItem)}
    }
    override fun getItemCount(): Int {
        return imgList.size
    }
    class ImgViewHolder (imgView: View, onClick: (ImageData) -> Unit) : RecyclerView.ViewHolder(imgView) {
        val img = imgView.findViewById<ImageView>(R.id.item_img)

    }
}
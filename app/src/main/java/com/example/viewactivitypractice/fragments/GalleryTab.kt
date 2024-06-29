package com.example.viewactivitypractice.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.viewactivitypractice.R
import com.example.viewactivitypractice.adapters.ImageAdapter
import com.example.viewactivitypractice.datas.ImageData

/**
 * A simple [Fragment] subclass.
 * Use the [GalleryTab.newInstance] factory method to
 * create an instance of this fragment.
 */
class GalleryTab : Fragment() {
    private val pickupimage = 100
    private var ImageURI : Uri? = null
    private lateinit var recyclerView: RecyclerView
    private var imgDataList: ArrayList<ImageData> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.gallery_tab_fragment, container, false)
        val addBtn = view.findViewById<Button>(R.id.pick_img_button)
        addBtn.setOnClickListener{
            // 갤러리에서 사진 가져오기
            val pickupImgFromGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(pickupImgFromGallery, pickupimage)
        }
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imgDataInitialize()
        recyclerView = view.findViewById(R.id.img_recyclerview)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = ImageAdapter(imgDataList)
    }

    // 사진 가져오면 imgData에 추가하기
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickupimage){
            ImageURI = data?.data
            Log.d("IMG_PICK", "successfully picked an img")
            imgDataList.add(ImageData(484, ImageURI))
        }
    }
    private fun imgDataInitialize(){
        imgDataList.add(ImageData(1, null))
        imgDataList.add(ImageData(2, null))
    }
}
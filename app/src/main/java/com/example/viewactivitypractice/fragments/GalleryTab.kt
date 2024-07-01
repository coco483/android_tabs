package com.example.viewactivitypractice.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.example.viewactivitypractice.DataBaseHandler
import com.example.viewactivitypractice.MainActivity
import com.example.viewactivitypractice.R
import com.example.viewactivitypractice.adapters.ImageAdapter
import com.example.viewactivitypractice.datas.ImageData
import com.example.viewactivitypractice.uriToBitmap
import java.io.InputStream




/**
 * A simple [Fragment] subclass.
 * Use the [GalleryTab.newInstance] factory method to
 * create an instance of this fragment.
 */
class GalleryTab : Fragment() {
    private lateinit var myDB: DataBaseHandler
    private val pickupimage = 100
    private var ImageURI : Uri? = null
    private lateinit var recyclerView: RecyclerView
    private var imgDataList: ArrayList<ImageData> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myDB = (activity as MainActivity).mydb
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.gallery_tab_fragment, container, false)
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

    private fun imgDataInitialize() {
        imgDataList = myDB.getAllImg()
    }


}
package com.example.viewactivitypractice.fragments

import android.content.Intent
import android.os.Bundle
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
    private lateinit var recyclerView: RecyclerView
    private lateinit var imgDataList: ArrayList<ImageData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_tab2, container, false)
        val pickImgBtn = view.findViewById<Button>(R.id.pick_img_button)
        pickImgBtn.setOnClickListener{
            //val pickupImgFromGallery = Intent(Intent.action)
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
    private fun imgDataInitialize(){
        imgDataList = ArrayList()
        imgDataList.add(ImageData(1, null))
        imgDataList.add(ImageData(2, null))
    }
}
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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.viewactivitypractice.DataBaseHandler
import com.example.viewactivitypractice.MainActivity
import com.example.viewactivitypractice.R
import com.example.viewactivitypractice.adapters.ImageAdapter
import com.example.viewactivitypractice.adapters.PostAdapter
import com.example.viewactivitypractice.datas.ImageData
import com.example.viewactivitypractice.datas.PostData
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
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = ImageAdapter(imgDataList) { imageData ->
            val post = myDB.getPostByImageId(imageData.id)
            if (post != null) {
                openPostDetailFragment(post)
            } else {
                Toast.makeText(requireContext(), "관련 포스트를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun imgDataInitialize() {
        imgDataList = myDB.getAllImg()
    }

    private fun openPostDetailFragment(post: PostData) {
        val postDetailFragment = PostDetailPage().apply {
            arguments = Bundle().apply {
                putInt("POST_ID", post.id)
                putString("POST_CONTENT", post.content)
                putString("POST_DATE", post.date)
                post.imageId?.let { putInt("POST_IMG_ID", it) }
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.blank_container, postDetailFragment)
            .addToBackStack(null)
            .commit()
    }
}
package com.example.viewactivitypractice.fragments

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.viewactivitypractice.DataBaseHandler
import com.example.viewactivitypractice.MainActivity
import com.example.viewactivitypractice.R
import com.example.viewactivitypractice.datas.PostData
import com.example.viewactivitypractice.uriToBitmap

/**
 * A simple [Fragment] subclass.
 * Use the [PostAddPage.newInstance] factory method to
 * create an instance of this fragment.
 */
class PostAddPage : Fragment() {
    private lateinit var myDB: DataBaseHandler
    private val pickupimage = 100
    private var ImageURI : Uri? = null
    private var bitImg : Bitmap? = null
    private var imgId: Int? = null
    private lateinit var imgView : ImageView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myDB = (activity as MainActivity).mydb
        val view =  inflater.inflate(R.layout.post_add_page, container, false)
        val uploadBtn = view.findViewById<Button>(R.id.post_upload_btn)
        val cancelBtn = view.findViewById<Button>(R.id.post_cancel_btn)
        imgView = view.findViewById<ImageView>(R.id.post_imageView)
        imgView.setOnClickListener{
            val pickupImgFromGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(pickupImgFromGallery, pickupimage)
        }
        uploadBtn.setOnClickListener{
            val content = view.findViewById<EditText>(R.id.content_ET).text.toString()
            if (content.length == 0) {
                Toast.makeText(requireContext(), "내용을 입력해 주세요", Toast.LENGTH_SHORT).show()
            } else {
                imgId = myDB.insertImg(bitImg).toInt()
                Log.d("PostAddImg", "id $imgId added")
                myDB.insertPost(PostData(-1, content, "", imageId = imgId))
                parentFragmentManager.beginTransaction().replace(R.id.blank_container, PostTab()).commit()
            }
        }
        cancelBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.blank_container, PostTab()).commit()
        }
        return view
    }
    // 사진 가져오면 imgData에 추가하기
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickupimage){
            ImageURI = data?.data
            Log.d("IMG_PICK", "successfully picked an img")
            //imgDataList.add(ImageData(484, uriToBitmap(ImageURI)) )
            bitImg = uriToBitmap(ImageURI, requireActivity())
            if (bitImg!=null) imgView.setImageBitmap(bitImg)
            //imgId = myDB.insertImg(bitImg).toInt()
            //Log.d("PostAddImg", "id $imgId added")
        }
    }

}
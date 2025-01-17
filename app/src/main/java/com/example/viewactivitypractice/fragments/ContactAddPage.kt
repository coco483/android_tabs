package com.example.viewactivitypractice.fragments

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.viewactivitypractice.MainActivity
import com.example.viewactivitypractice.R
import com.example.viewactivitypractice.datas.ContactData
import com.example.viewactivitypractice.saveBitmapToInternalStorage
import com.example.viewactivitypractice.uriToBitmap

class ContactAddPage: Fragment() {
    private var bitImg : Bitmap? = null
    private var imgId: Int? = null
    private lateinit var imgView : ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val myDB = (activity as MainActivity).mydb
        val view =  inflater.inflate(R.layout.contact_add_page, container, false)
        val confirmBtn = view.findViewById<Button>(R.id.confirm_btn)
        val cancelBtn = view.findViewById<Button>(R.id.cancel_btn)
        imgView = view.findViewById<ImageView>(R.id.contact_imageView)
        imgView.setOnClickListener {
            val options = arrayOf("Take Photo", "Choose from Gallery")
            AlertDialog.Builder(requireContext())
                .setTitle("Select Image Source")
                .setItems(options) { dialog, which ->
                    when (which) {
                        0 -> startCamera()
                        1 -> pickFromGallery()
                    }
                }
                .show()
        }
        confirmBtn.setOnClickListener{
            val name = view.findViewById<EditText>(R.id.name_ET).text.toString()
            val phonenum = view.findViewById<EditText>(R.id.phoneNum_ET).text.toString()
            if (phonenum.length != 11) {
                Toast.makeText(requireContext(), "전화번호 11자리를 모두 입력해 주세요", Toast.LENGTH_SHORT).show()
            } else if (name == ""){
                Toast.makeText(requireContext(), "이름을 입력해 주세요", Toast.LENGTH_SHORT).show()
            } else {
                val imgPath = bitImg?.let{bitimg -> saveBitmapToInternalStorage(requireContext(),bitimg) }
                imgId = imgPath?.let { it1 -> myDB.insertImg(it1).toInt() }
                Log.d("PostAddImg", "id $imgId added to $imgPath")
                myDB.insertContact(ContactData(-1, name, phonenum, imgId))
                parentFragmentManager.beginTransaction().replace(R.id.blank_container, ContactTab()).commit()
            }
        }
        cancelBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.blank_container, ContactTab()).commit()
        }
        return view
    }
    private fun startCamera(){
        val cameraPermissionCheck = ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.CAMERA
        )
        if (cameraPermissionCheck != PackageManager.PERMISSION_GRANTED) { // 권한이 없는 경우
            Log.d("postCam", "request for camera permission")
            requestCameraPermission.launch(android.Manifest.permission.CAMERA)
        } else { // 권한이 있는 경우
            Log.d("postCam", "we have camera permission")
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePictureByCamera.launch(takePictureIntent)
        }
    }
    private fun pickFromGallery(){
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_IMAGES
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }
        val galleryPermissionCheck = ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        )
        if (galleryPermissionCheck != PackageManager.PERMISSION_GRANTED) { // 권한이 없는 경우
            Log.d("postCam", "request for gallery access permission")
            requestGalleryPermission.launch(permission)
        } else { // 권한이 있는 경우
            Log.d("postCam", "we have gallery access permission")
            val pickUpImgFromGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            pickImageFromGallery.launch(pickUpImgFromGallery)
        }
    }
    private val pickImageFromGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val imageUri: Uri? = result.data?.data
            Log.d("IMG_PICK", "Successfully picked an img, $imageUri")
            bitImg = uriToBitmap(imageUri, requireActivity())
            bitImg?.let { imgView.setImageBitmap(it) }
        }
    }
    private val takePictureByCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as Bitmap
            Log.d("CAMERA", "Successfully took a picture, $imageBitmap")
            bitImg = imageBitmap
            bitImg?.let { imgView.setImageBitmap(it) }
        }
    }

    private  val requestCameraPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { isGranted ->
        when(isGranted) {
            true -> {
                Toast.makeText(requireContext(),"카메라 권한 허가",Toast.LENGTH_SHORT).show()
                startCamera()}
            false -> {
                Toast.makeText(requireContext(),"카메라 권한 거부", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private  val requestGalleryPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { isGranted ->
        when(isGranted) {
            true -> {
                Toast.makeText(requireContext(),"갤러리 권한 허가",Toast.LENGTH_SHORT).show()
                pickFromGallery()}
            false -> {
                Toast.makeText(requireContext(),"갤러리 권한 거부", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
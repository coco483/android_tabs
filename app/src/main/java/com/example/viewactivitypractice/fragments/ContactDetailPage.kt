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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.viewactivitypractice.DataBaseHandler
import com.example.viewactivitypractice.MainActivity
import com.example.viewactivitypractice.R
import com.example.viewactivitypractice.adapters.PostAdapter
import com.example.viewactivitypractice.datas.ContactData
import com.example.viewactivitypractice.datas.PostData
import com.example.viewactivitypractice.saveBitmapToInternalStorage
import com.example.viewactivitypractice.uriToBitmap


class ContactDetailPage : Fragment() {

    private var contactId: Int = -1  // 인스턴스 변수로 ID 저장
    private lateinit var contactName: String
    private lateinit var contactPhoneNum: String
    private var contactImageId: Int? = null
    private lateinit var myDB: DataBaseHandler

    private var isImageChanged : Boolean = false
    private var bitImg : Bitmap? =null
    private var imgId: Int? = null
    private lateinit var imgView : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            contactId = it.getInt("CONTACT_ID")
            contactName = it.getString("EXTRA_CONTACT_NAME").toString()
            contactPhoneNum = it.getString("EXTRA_CONTACT_PHONE").toString()
            contactImageId = it.getInt("CONTACT_IMG_ID")
            Log.d("ContactDetail", "id: $contactId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myDB = (activity as MainActivity).mydb
        val view =  inflater.inflate(R.layout.contact_detail_page, container, false)
        view.findViewById<EditText>(R.id.name_ET).setText(contactName)
        view.findViewById<EditText>(R.id.phoneNum_ET).setText(contactPhoneNum)

        val editBtn = view.findViewById<Button>(R.id.edit_btn)
        val deleteBtn = view.findViewById<Button>(R.id.delete_btn)
        imgView = view.findViewById<ImageView>(R.id.contact_imageView)
        bitImg = myDB.getImgById(contactImageId)
        imgView.setImageBitmap(bitImg)

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

        editBtn.setOnClickListener {

            val newName = view.findViewById<EditText>(R.id.name_ET).text.toString()
            val newPhoneNum = view.findViewById<EditText>(R.id.phoneNum_ET).text.toString()
            if (newPhoneNum.length != 11) {
                Toast.makeText(requireContext(), "전화번호 11자리를 모두 입력해 주세요", Toast.LENGTH_SHORT).show()
            } else if (newName == "") {
                Toast.makeText(requireContext(), "이름을 입력해 주세요", Toast.LENGTH_SHORT).show()
            } else {
                if(isImageChanged) {
                    val imgPath = bitImg?.let { bitimg -> saveBitmapToInternalStorage(requireContext(), bitimg) }
                    contactImageId?.let { it1 -> myDB.deleteImgById(it1) }
                    imgId = imgPath?.let { it1 -> myDB.insertImg(it1).toInt() }
                    Log.d("ContactDetailImg", "isImgChanged: $isImageChanged, bitImg: $bitImg imgPath: $imgPath imgId: $imgId")
                }

                myDB.updateContact(ContactData(contactId, newName, newPhoneNum, imgId)) // 연락처 업데이트
                parentFragmentManager.beginTransaction().replace(R.id.blank_container, ContactTab())
                    .commit() // 변경 사항 반영
            }
        }

        deleteBtn.setOnClickListener {
            if (contactId != -1) {
                myDB.deleteContactById(contactId) // ID를 사용하여 삭제
                Toast.makeText(context, "Contact deleted successfully", Toast.LENGTH_SHORT).show()
                parentFragmentManager.beginTransaction().replace(R.id.blank_container, ContactTab())
                    .commit()
            } else {
                Toast.makeText(context, "Invalid contact ID", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val postDataList = myDB.getPostByContactId(contactId)
        setPostRecyclerView(view, postDataList)
    }
    fun setPostRecyclerView(view: View, postDatas: ArrayList<PostData>) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.contact_tagged_post_RV)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = PostAdapter(myDB, postDatas, gotoPostDetail)
    }
    val gotoPostDetail: (PostData) -> Unit = { post ->
        // 클릭된 아이템의 연락처 정보를 PostDetailPage 프래그먼트에 전달
        val detailPage = PostDetailPage().apply {
            arguments = Bundle().apply {
                putInt("POST_ID", post.id)  // 가정: PostData에 id 필드가 있다고 가정
                putString("POST_CONTENT", post.content)
                post.imageId?.let { putInt("POST_IMG_ID", it) }
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.blank_container, detailPage)
            .addToBackStack(null)  // Back stack을 사용하여 뒤로 가기 버튼으로 이전 화면으로 돌아갈 수 있도록 함
            .commit()
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
            isImageChanged = true
            Log.d("IMG_PICK", "Successfully picked an img, $imageUri")
            bitImg = uriToBitmap(imageUri, requireActivity())
            bitImg?.let { imgView.setImageBitmap(it) }
            Log.d("IMG_PICK", "bitImg: $bitImg")
        }
    }
    private val takePictureByCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as Bitmap
            isImageChanged = true
            Log.d("CAMERA", "Successfully took a picture, $imageBitmap")
            bitImg = imageBitmap
            bitImg?.let { imgView.setImageBitmap(it) }

            Log.d("CAMERA", "bitImg: $bitImg")
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
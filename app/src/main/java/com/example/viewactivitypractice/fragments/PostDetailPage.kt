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
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.viewactivitypractice.DataBaseHandler
import com.example.viewactivitypractice.MainActivity
import com.example.viewactivitypractice.R
import com.example.viewactivitypractice.datas.ContactData
import com.example.viewactivitypractice.datas.PostData
import com.example.viewactivitypractice.saveBitmapToInternalStorage
import com.example.viewactivitypractice.uriToBitmap


/**
 * A simple [Fragment] subclass.
 * Use the [PostDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PostDetailPage : Fragment() {
    private lateinit var myDB: DataBaseHandler

    private var postId: Int = -1  // onCreate에 선언으로 바꿔보기
    private lateinit var postContent: String
    private var postImgId : Int? = null
    private var tagListText: String? = ""
    private var isImageChanged : Boolean = false

    private var bitImg : Bitmap? = null
    private var imgId: Int? = null
    private lateinit var imgView : ImageView
    private lateinit var contactList : Array<ContactData>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            postId = it.getInt("POST_ID")
            postContent = it.getString("POST_CONTENT").toString()
            postImgId = it.getInt("POST_IMG_ID")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myDB = (activity as MainActivity).mydb
        val view = inflater.inflate(R.layout.post_detail_page, container, false)
        view.findViewById<EditText>(R.id.content_ET).setText(postContent)


        imgView = view.findViewById<ImageView>(R.id.post_imageView)
        val bitImg = myDB.getImgById(postImgId)
        bitImg?.let{ img -> imgView.setImageBitmap(img)}

        // autocomplete tag search
        val contactIdSet : Set<Int> =  myDB.getAllContactIdByPostId(postId)
        for(contactId in contactIdSet){
            val contactName = myDB.getContactByContactId(contactId)?.name ?:""
            tagListText += ("@$contactName, ")
        }
        Log.d("check tagListText", "$tagListText")
        if (tagListText!!.isNotEmpty()) {
            // Remove the trailing comma and space
            tagListText = tagListText!!.substring(0, tagListText!!.length - 2)
        }
        val tagList = view.findViewById<TextView>(R.id.tag_list_TV)
        tagList.text = tagListText
        val tagIdSet : MutableSet<Int> = mutableSetOf()

        var tagText = tagList.text.toString()
        val tagListStrList : ArrayList<String> = arrayListOf()
        val tagAutoComplete = view.findViewById<AutoCompleteTextView>(R.id.tag_AutoTV)
        setContactList()
        val tagAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, contactList)
        tagAutoComplete.setAdapter(tagAdapter)
        tagAutoComplete.setOnItemClickListener { parent, _, position, _ ->
            val item = parent.getItemAtPosition(position)
            if (item is ContactData) {
                Log.d("PostTag", "clicked ${item.name}")
                tagListStrList.add(item.name)
                tagIdSet.add(item.id)
                tagText += ("@"+ item.name + " ")
                tagList.text = tagText
            }
            tagAutoComplete.text = null

        }

        val editBtn = view.findViewById<Button>(R.id.post_Edit_btn)
        editBtn.setOnClickListener {

            val newContent = view.findViewById<EditText>(R.id.content_ET).text.toString()
            if (newContent == "") {
                Toast.makeText(requireContext(), "내용을 입력해 주세요", Toast.LENGTH_SHORT).show()
            } else {
                if(isImageChanged) {
                    val imgPath = bitImg?.let { bitimg -> saveBitmapToInternalStorage(requireContext(), bitimg)
                    }
                    postImgId?.let { it1 -> myDB.deleteImgById(it1) }
                    imgId = imgPath?.let { it1 -> myDB.insertImg(it1).toInt() }
                }
                myDB.updatePost(PostData(postId, newContent, "", imgId), tagIdSet) // 연락처 업데이트
                parentFragmentManager.beginTransaction().replace(R.id.blank_container, PostTab())
                    .commit() // 변경 사항 반영
            }
        }

        val deleteBtn = view.findViewById<Button>(R.id.post_delete_btn)
        deleteBtn.setOnClickListener {
            myDB.deletePostById(postId)
            parentFragmentManager.beginTransaction().replace(R.id.blank_container, PostTab())
                .commit() // 변경 사항 반영
        }


        // start camera or open gallery when user clicks image
        imgView = view.findViewById<ImageView>(R.id.post_imageView)
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

        return view
    }

    private fun setContactList(){
        val allContact = myDB.getAllContact()
        contactList = allContact.toArray(arrayOfNulls<ContactData>(allContact.size))
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
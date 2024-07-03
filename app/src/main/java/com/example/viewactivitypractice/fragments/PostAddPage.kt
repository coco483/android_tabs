package com.example.viewactivitypractice.fragments

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
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
import androidx.fragment.app.FragmentActivity
import com.example.viewactivitypractice.DataBaseHandler
import com.example.viewactivitypractice.MainActivity
import com.example.viewactivitypractice.R
import com.example.viewactivitypractice.datas.ContactData
import com.example.viewactivitypractice.datas.PostData
import com.example.viewactivitypractice.uriToBitmap
import com.example.viewactivitypractice.saveBitmapToInternalStorage
/**
 * A simple [Fragment] subclass.
 * Use the [PostAddPage.newInstance] factory method to
 * create an instance of this fragment.
 */
class PostAddPage : Fragment() {
    private lateinit var myDB: DataBaseHandler
    private var bitImg : Bitmap? = null
    private var imgId: Int? = null
    private lateinit var imgView : ImageView
    private lateinit var contactList : Array<ContactData>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myDB = (activity as MainActivity).mydb
        val view =  inflater.inflate(R.layout.post_add_page, container, false)
//        var tagIdList: ArrayList<Int> = arrayListOf<Int>()
        val uploadBtn = view.findViewById<Button>(R.id.post_Edit_btn)
        val cancelBtn = view.findViewById<Button>(R.id.post_delete_btn)
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
        // autocomplete tag search
        val tagIdSet : MutableSet<Int> = mutableSetOf()
        val tagList = view.findViewById<TextView>(R.id.tag_list_TV)
        /*var tagText = tagList.text.toString()
        tagList.setText("")*/
        val tagSpannable = SpannableStringBuilder()
        val existingText = tagList.text

        val tagListStrList : ArrayList<String> = arrayListOf()
        val tagAutoComplete = view.findViewById<AutoCompleteTextView>(R.id.tag_AutoTV)
        setContactList()
        val tagAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, contactList)
        tagAutoComplete.setAdapter(tagAdapter)
        tagAutoComplete.setOnItemClickListener { parent, _, position, _ ->
            val item = parent.getItemAtPosition(position)
            val spannable = SpannableStringBuilder()
            if (item is ContactData) {

                Log.d("PostTag", "clicked ${item.name}")
                tagListStrList.add(item.name)
                tagIdSet.add(item.id)/*
                tagText += ("@"+ item.name + " ")*/
                // spannable

                val start = spannable.length
                spannable.append("@${item.name}")

                // Apply styles
                spannable.setSpan(
                    StyleSpan(Typeface.ITALIC),
                    start,
                    spannable.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.setSpan(
                    ForegroundColorSpan(Color.BLUE),
                    start,
                    spannable.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.setSpan(
                    UnderlineSpan(),
                    start,
                    spannable.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                // Apply ClickableSpan
                spannable.setSpan(
                    object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            val context = widget.context
                            if (context is FragmentActivity) {
                                val contactDetailFragment = ContactDetailPage().apply {
                                    arguments = Bundle().apply {
                                        putInt("CONTACT_ID", item.id)
                                        putString("EXTRA_CONTACT_NAME", item.name)
                                        putString("EXTRA_CONTACT_PHONE", item.phonenumber)
                                        item.imageId?.let { putInt("CONTACT_IMG_ID", it) }
                                    }
                                }
                                (context as MainActivity).updateBottomNavigationView(R.id.tab1)
                                context.supportFragmentManager.beginTransaction()
                                    .replace(R.id.blank_container, contactDetailFragment)
                                    .addToBackStack(null)
                                    .commit()


                            } // if
                        } // onClick
                    },
                    start,
                    spannable.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                ) // setSpan
                Log.d("checkClickStart", "start: $start")

                spannable.append(" ")
            } // if item check end

            tagSpannable.append(spannable)
            tagList.text = tagSpannable
            tagList.movementMethod = LinkMovementMethod.getInstance()
            tagAutoComplete.text = null

        }
        // insert to DB and go back to PostTab()
        uploadBtn.setOnClickListener{
            val content = view.findViewById<EditText>(R.id.content_ET).text.toString()
            if (content.length == 0) {
                Toast.makeText(requireContext(), "내용을 입력해 주세요", Toast.LENGTH_SHORT).show()
            } else {
                val imgPath = bitImg?.let{bitimg -> saveBitmapToInternalStorage(requireContext(),bitimg)}
                imgId = imgPath?.let { it1 -> myDB.insertImg(it1).toInt() }
                Log.d("PostAddImg", "id $imgId added to $imgPath")
                myDB.insertPost(PostData(-1, content, "", imgId), tagIdSet)
                parentFragmentManager.beginTransaction().replace(R.id.blank_container, PostTab()).commit()
            }
        }
        cancelBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.blank_container, PostTab()).commit()
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
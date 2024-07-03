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
import com.example.viewactivitypractice.saveBitmapToInternalStorage
import com.example.viewactivitypractice.uriToBitmap

class PostDetailPage : Fragment() {
    private lateinit var myDB: DataBaseHandler

    private var postId: Int = -1
    private lateinit var postContent: String
    private var postImgId: Int? = null
    private var isImageChanged: Boolean = false

    private var bitImg: Bitmap? = null
    private var imgId: Int? = null
    private lateinit var imgView: ImageView
    private lateinit var contactList: Array<ContactData>

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
        bitImg?.let { img -> imgView.setImageBitmap(img) }

        // autocomplete tag search
        val tagIdSet: MutableSet<Int> = mutableSetOf()
        val tagList = view.findViewById<TextView>(R.id.tag_list_TV)
        val tagSpannable = SpannableStringBuilder()
        val contactIdSet: Set<Int> = myDB.getAllContactIdByPostId(postId)

        for (contactId in contactIdSet) {
            val contact = myDB.getContactByContactId(contactId)
            contact?.let {
                val spannable = createSpannableForContact(it)
                tagSpannable.append(spannable).append(" ")
                tagIdSet.add(contactId)
            }
        }

        tagList.text = tagSpannable
        tagList.movementMethod = LinkMovementMethod.getInstance()

        val tagAutoComplete = view.findViewById<AutoCompleteTextView>(R.id.tag_AutoTV)
        setContactList()
        val tagAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, contactList)
        tagAutoComplete.setAdapter(tagAdapter)
        tagAutoComplete.setOnItemClickListener { parent, _, position, _ ->
            val item = parent.getItemAtPosition(position)
            if (item is ContactData) {
                val spannable = createSpannableForContact(item)
                tagSpannable.append(spannable).append(" ")
                tagList.text = tagSpannable
                tagAutoComplete.text = null
                tagIdSet.add(item.id)
            }
        }

        val editBtn = view.findViewById<Button>(R.id.post_Edit_btn)
        editBtn.setOnClickListener {
            val newContent = view.findViewById<EditText>(R.id.content_ET).text.toString()
            if (newContent.isEmpty()) {
                Toast.makeText(requireContext(), "내용을 입력해 주세요", Toast.LENGTH_SHORT).show()
            } else {
                if (isImageChanged) {
                    val imgPath = bitImg?.let { bitimg -> saveBitmapToInternalStorage(requireContext(), bitimg) }
                    postImgId?.let { myDB.deleteImgById(it) }
                    imgId = imgPath?.let { myDB.insertImg(it).toInt() }
                }
                myDB.updatePost(PostData(postId, newContent, "", imgId), tagIdSet)
                parentFragmentManager.beginTransaction().replace(R.id.blank_container, PostTab()).commit()
            }
        }

        val deleteBtn = view.findViewById<Button>(R.id.post_delete_btn)
        deleteBtn.setOnClickListener {
            myDB.deletePostById(postId)
            parentFragmentManager.beginTransaction().replace(R.id.blank_container, PostTab()).commit()
        }

        imgView.setOnClickListener {
            val options = arrayOf("Take Photo", "Choose from Gallery")
            AlertDialog.Builder(requireContext())
                .setTitle("Select Image Source")
                .setItems(options) { _, which ->
                    when (which) {
                        0 -> startCamera()
                        1 -> pickFromGallery()
                    }
                }
                .show()
        }

        return view
    }

    private fun createSpannableForContact(contact: ContactData): SpannableString {
        val spannable = SpannableString("@${contact.name}")

        spannable.setSpan(
            StyleSpan(Typeface.ITALIC),
            0,
            spannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(Color.BLUE),
            0,
            spannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            UnderlineSpan(),
            0,
            spannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            object : ClickableSpan() {
                override fun onClick(widget: View) {
                    val context = widget.context
                    if (context is FragmentActivity) {
                        val contactDetailFragment = ContactDetailPage().apply {
                            arguments = Bundle().apply {
                                putInt("CONTACT_ID", contact.id)
                                putString("EXTRA_CONTACT_NAME", contact.name)
                                putString("EXTRA_CONTACT_PHONE", contact.phonenumber)
                                contact.imageId?.let { putInt("CONTACT_IMG_ID", it) }
                            }
                        }
                        (context as MainActivity).updateBottomNavigationView(R.id.tab1)
                        context.supportFragmentManager.beginTransaction()
                            .replace(R.id.blank_container, contactDetailFragment)
                            .addToBackStack(null)
                            .commit()
                    }
                }
            },
            0,
            spannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannable
    }

    private fun setContactList() {
        val allContact = myDB.getAllContact()
        contactList = allContact.toArray(arrayOfNulls<ContactData>(allContact.size))
    }

    private fun startCamera() {
        val cameraPermissionCheck = ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.CAMERA
        )
        if (cameraPermissionCheck != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission.launch(android.Manifest.permission.CAMERA)
        } else {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePictureByCamera.launch(takePictureIntent)
        }
    }

    private fun pickFromGallery() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_IMAGES
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }
        val galleryPermissionCheck = ContextCompat.checkSelfPermission(requireContext(), permission)
        if (galleryPermissionCheck != PackageManager.PERMISSION_GRANTED) {
            requestGalleryPermission.launch(permission)
        } else {
            val pickUpImgFromGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            pickImageFromGallery.launch(pickUpImgFromGallery)
        }
    }

    private val pickImageFromGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val imageUri: Uri? = result.data?.data
            isImageChanged = true
            bitImg = uriToBitmap(imageUri, requireActivity())
            bitImg?.let { imgView.setImageBitmap(it) }
        }
    }

    private val takePictureByCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as Bitmap
            isImageChanged = true
            bitImg = imageBitmap
            bitImg?.let { imgView.setImageBitmap(it) }
        }
    }

    private val requestCameraPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            startCamera()
        } else {
            Toast.makeText(requireContext(), "카메라 권한 거부", Toast.LENGTH_SHORT).show()
        }
    }

    private val requestGalleryPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            pickFromGallery()
        } else {
            Toast.makeText(requireContext(), "갤러리 권한 거부", Toast.LENGTH_SHORT).show()
        }
    }
}

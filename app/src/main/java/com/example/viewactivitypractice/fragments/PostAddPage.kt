package com.example.viewactivitypractice.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.viewactivitypractice.MainActivity
import com.example.viewactivitypractice.R

/**
 * A simple [Fragment] subclass.
 * Use the [PostAddPage.newInstance] factory method to
 * create an instance of this fragment.
 */
class PostAddPage : Fragment() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val myDB = (activity as MainActivity).mydb
        val view =  inflater.inflate(R.layout.post_add_page, container, false)
        val uploadBtn = view.findViewById<Button>(R.id.post_upload_btn)
        val cancelBtn = view.findViewById<Button>(R.id.post_cancel_btn)

        uploadBtn.setOnClickListener{
            val content = view.findViewById<EditText>(R.id.content_ET).text.toString()
            if (content.length == 0) {
                Toast.makeText(requireContext(), "내용을 입력해 주세요", Toast.LENGTH_SHORT).show()
            } else {
                myDB.insertPost(content)
                parentFragmentManager.beginTransaction().replace(R.id.blank_container, PostTab()).commit()
            }
        }
        cancelBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.blank_container, PostTab()).commit()
        }
        return view
    }

}
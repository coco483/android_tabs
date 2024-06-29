package com.example.viewactivitypractice.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.viewactivitypractice.MainActivity
import com.example.viewactivitypractice.R

/**
 * A simple [Fragment] subclass.
 * Use the [PostAddPage.newInstance] factory method to
 * create an instance of this fragment.
 */
class PostAddPage : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val myDB = (activity as MainActivity).mydb
        val view =  inflater.inflate(R.layout.post_add_page, container, false)
        val uploadBtn = view.findViewById<Button>(R.id.post_upload_btn)
        val cancelBtn = view.findViewById<Button>(R.id.post_cancel_btn)

        /uploadBtn.setOnClickListener{
            val content = view.findViewById<EditText>(R.id.content_ET).text.toString()
            val phonenum = view.findViewById<EditText>(R.id.)text.toString()
            if (phonenum.length != 11) {
                Toast.makeText(requireContext(), "전화번호 11자리를 모두 입력해 주세요", Toast.LENGTH_SHORT).show()
            } else if (name == ""){
                Toast.makeText(requireContext(), "이름을 입력해 주세요", Toast.LENGTH_SHORT).show()
            } else {
                myDB.insertContact(name, phonenum)
                parentFragmentManager.beginTransaction().replace(R.id.blank_container, ContactTab()).commit()
            }
        }
        cancelBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.blank_container, ContactTab()).commit()
        }*/
        return view
    }

}
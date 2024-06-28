package com.example.viewactivitypractice.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.viewactivitypractice.MainActivity
import com.example.viewactivitypractice.R
import kotlin.contracts.Effect

class AddContactPage: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var mydb = (activity as MainActivity).mydb
        val view =  inflater.inflate(R.layout.add_contact_page, container, false)
        val confirm_btn = view.findViewById<Button>(R.id.confirm_btn)
        val cancel_btn = view.findViewById<Button>(R.id.cancel_btn)

        confirm_btn.setOnClickListener{
            var name = view.findViewById<EditText>(R.id.name_ET).text.toString()
            var phonenum = view.findViewById<EditText>(R.id.phoneNum_ET).text.toString()
            if (phonenum.length != 11) {
                Toast.makeText(requireContext(), "전화번호 11자리를 모두 입력해 주세요", Toast.LENGTH_SHORT).show()
            } else if (name == ""){
                Toast.makeText(requireContext(), "이름을 입력해 주세요", Toast.LENGTH_SHORT).show()
            } else {
                mydb.insertContact(name, phonenum)
                parentFragmentManager.beginTransaction().replace(R.id.blank_container, ContactTab()).commit()
            }
        }
        cancel_btn.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.blank_container, ContactTab()).commit()
        }
        return view
    }
}
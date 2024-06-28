package com.example.viewactivitypractice.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.viewactivitypractice.MainActivity
import com.example.viewactivitypractice.R


class ContactDetailPage : Fragment() {

    private var contactId: Int = -1  // 인스턴스 변수로 ID 저장

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            contactId = it.getInt("COL_ID", -1)
            Log.d("ContactDetail", "id: $contactId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var mydb = (activity as MainActivity).mydb
        val view =  inflater.inflate(R.layout.contact_detail_page, container, false)

        val editBtn = view.findViewById<Button>(R.id.edit_btn)
        val deleteBtn = view.findViewById<Button>(R.id.delete_btn)

        editBtn.setOnClickListener {

            val newName = view.findViewById<EditText>(R.id.name_ET).text.toString()
            val newPhoneNum = view.findViewById<EditText>(R.id.phoneNum_ET).text.toString()

            mydb.updateContact(contactId, newName, newPhoneNum) // 연락처 업데이트
            parentFragmentManager.beginTransaction().replace(R.id.blank_container, ContactTab()).commit() // 변경 사항 반영
        }

        deleteBtn.setOnClickListener {
            if (contactId != -1) {
                mydb.deleteContactById(contactId) // ID를 사용하여 삭제
                Toast.makeText(context, "Contact deleted successfully", Toast.LENGTH_SHORT).show()
                parentFragmentManager.beginTransaction().replace(R.id.blank_container, ContactTab())
                    .commit()
            } else {
                Toast.makeText(context, "Invalid contact ID", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

}
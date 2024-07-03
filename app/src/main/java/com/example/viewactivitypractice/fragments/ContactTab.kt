package com.example.viewactivitypractice.fragments

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.viewactivitypractice.DataBaseHandler
import com.example.viewactivitypractice.MainActivity
import com.example.viewactivitypractice.R
import com.example.viewactivitypractice.adapters.ContactAdapter
import com.example.viewactivitypractice.datas.ContactData

/**
 * A simple [Fragment] subclass.
 * Use the [ContactTab.newInstance] factory method to
 * create an instance of this fragment.
 */


class ContactTab : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var contactDataList: ArrayList<ContactData>
    private lateinit var myDB: DataBaseHandler
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myDB = (activity as MainActivity).mydb
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.contact_tab_fragment, container, false)
        val add_btn = view.findViewById<ImageButton>(R.id.add_contact_button)
        add_btn.setOnClickListener{
            parentFragmentManager.beginTransaction().replace(R.id.blank_container, ContactAddPage()).commit()
        }
        val searchBtn = view.findViewById<ImageButton>(R.id.contact_search_btn)
        searchBtn.setOnClickListener{
            val searchText = view.findViewById<EditText>(R.id.contact_search_ET).text.toString()
            contactDataList = myDB.getContactIncludes(searchText)
            setRecyclerView(view, contactDataList)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contactDataList = myDB.getAllContact()

        setRecyclerView(view, contactDataList)
    }

    fun setRecyclerView(view: View, contactDatas: ArrayList<ContactData>){
        recyclerView = view.findViewById(R.id.numRecycler)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = ContactAdapter(contactDatas, gotoContactDetail, deleteContact)
    }
    val gotoContactDetail: (ContactData) -> Unit = {contact ->
        // 클릭된 아이템의 연락처 정보를 ContactDetailPage 프래그먼트에 전달
        val detailFragment = ContactDetailPage().apply {
            arguments = Bundle().apply {
                putInt("CONTACT_ID", contact.id)  // 가정: ContactData에 id 필드가 있다고 가정
                putString("EXTRA_CONTACT_NAME", contact.name)
                putString("EXTRA_CONTACT_PHONE",contact.phonenumber)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.blank_container, detailFragment)
            .addToBackStack(null)  // Back stack을 사용하여 뒤로 가기 버튼으로 이전 화면으로 돌아갈 수 있도록 함
            .commit()
    }
    val deleteContact: (Int) -> Unit = { contactId ->
        myDB.deleteContactById(contactId) // ID를 사용하여 삭제
        Toast.makeText(requireContext(), "Contact deleted successfully", Toast.LENGTH_SHORT).show()
        parentFragmentManager.beginTransaction().replace(R.id.blank_container, ContactTab())
            .commit()
    }
}
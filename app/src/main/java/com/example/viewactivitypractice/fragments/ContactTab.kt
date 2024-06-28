package com.example.viewactivitypractice.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private lateinit var adapter: ContactAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var contactDataList: ArrayList<ContactData>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_tab1, container, false)
        val add_btn = view.findViewById<Button>(R.id.add_contact_button)
        add_btn.setOnClickListener{
            parentFragmentManager.beginTransaction().replace(R.id.blank_container, AddContactPage()).commit()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataInitialize()
        recyclerView = view.findViewById(R.id.numRecycler)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = ContactAdapter(contactDataList) { contact ->
            // 클릭된 아이템의 연락처 정보를 ContactDetailPage 프래그먼트에 전달
            val detailFragment = ContactDetailPage().apply {
                arguments = Bundle().apply {
                    putInt("CONTACT_ID", contact.id)  // 가정: ContactData에 id 필드가 있다고 가정
                    putString("EXTRA_CONTACT_NAME", contact.name)
                    putString("EXTRA_CONTACT_PHONE", contact.phonenumber)
                }
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.blank_container, detailFragment)
                .addToBackStack(null)  // Back stack을 사용하여 뒤로 가기 버튼으로 이전 화면으로 돌아갈 수 있도록 함
                .commit()
        }
    }
    private fun dataInitialize() {
        //val jsonString = readJsonFromAssets(requireActivity(), "contact_info.json")
        //contactDataList = parseJsonToNumberDatas(jsonString)
        var mydb = (activity as MainActivity).mydb
        contactDataList = mydb.getAllContact()
    }
}
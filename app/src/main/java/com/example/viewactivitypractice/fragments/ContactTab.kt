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
        recyclerView.adapter = ContactAdapter(contactDataList)
    }
    private fun dataInitialize() {
        //val jsonString = readJsonFromAssets(requireActivity(), "contact_info.json")
        //contactDataList = parseJsonToNumberDatas(jsonString)
        var mydb = (activity as MainActivity).mydb
        contactDataList = mydb.getAllContact()
    }
}
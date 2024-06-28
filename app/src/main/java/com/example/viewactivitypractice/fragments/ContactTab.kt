package com.example.viewactivitypractice.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.viewactivitypractice.adapters.ContactAdapter
import com.example.viewactivitypractice.R
import com.example.viewactivitypractice.datas.ContactData
import com.example.viewactivitypractice.parseJsonToNumberDatas
import com.example.viewactivitypractice.readJsonFromAssets

/**
 * A simple [Fragment] subclass.
 * Use the [ContactTab.newInstance] factory method to
 * create an instance of this fragment.
 */

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ContactTab : Fragment() {

    private lateinit var adapter: ContactAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var contactDataList: ArrayList<ContactData>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab1, container, false)
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
        val jsonString = readJsonFromAssets(requireActivity(), "contact_info.json")
        contactDataList = parseJsonToNumberDatas(jsonString)

    }
}
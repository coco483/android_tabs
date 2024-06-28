package com.example.viewactivitypractice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Tab1.newInstance] factory method to
 * create an instance of this fragment.
 */
class Tab1 : Fragment() {

    private lateinit var adapter: MyAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var numberDataArrayList: ArrayList<NumberDatas>

    lateinit var contactname: Array<String>
    lateinit var number: Array<String>
    lateinit var numberDatas: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.numRecycler)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = MyAdapter(numberDataArrayList)
        recyclerView.adapter = adapter
    }


    private fun dataInitialize() {

        numberDataArrayList = arrayListOf<NumberDatas>()

        contactname = arrayOf(
            getString(R.string.contact_name_1),
            getString(R.string.contact_name_2),
            getString(R.string.contact_name_3),
            getString(R.string.contact_name_4),
            getString(R.string.contact_name_5),
            getString(R.string.contact_name_6),
            getString(R.string.contact_name_7),
            getString(R.string.contact_name_8),
            getString(R.string.contact_name_9),
            getString(R.string.contact_name_10),
        )

        number = arrayOf(
            getString(R.string.contact_number_1),
            getString(R.string.contact_number_2),
            getString(R.string.contact_number_3),
            getString(R.string.contact_number_4),
            getString(R.string.contact_number_5),
            getString(R.string.contact_number_6),
            getString(R.string.contact_number_7),
            getString(R.string.contact_number_8),
            getString(R.string.contact_number_9),
            getString(R.string.contact_number_10),
        )

        for (i in contactname.indices) {

            val numberData = NumberDatas(contactname[i], number[i])
            numberDataArrayList.add(numberData)
        }
    }
}
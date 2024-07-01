package com.example.viewactivitypractice.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.viewactivitypractice.DataBaseHandler
import com.example.viewactivitypractice.MainActivity
import com.example.viewactivitypractice.R
import com.example.viewactivitypractice.adapters.TagAdapter
import com.example.viewactivitypractice.datas.PostData
import com.example.viewactivitypractice.datas.TagData

/**
 * tagaddpage, taddetailpage, tagtab에 보일 리싸이클러 뷰
 */
class TagTab : Fragment() {

    private lateinit var myDB : DataBaseHandler
    private lateinit var recyclerView: RecyclerView
    private lateinit var tagAdapter: TagAdapter
    private lateinit var tagList: ArrayList<TagData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tag_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myDB = (activity as MainActivity).mydb

        // RecyclerView 설정
        recyclerView = view.findViewById(R.id.postRecycler)
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        // TagAdapter 설정
        tagAdapter = TagAdapter(myDB, tagList) { tagData ->
            openContactDetail(tagData.contactId)
        }
        recyclerView.adapter = tagAdapter

    }



    private fun openContactDetail(contactId: Int) {
        val contactDetailFragment = ContactDetailPage().apply {
            arguments = Bundle().apply {
                putInt("CONTACT_ID", contactId)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.blank_container, contactDetailFragment)
            .addToBackStack(null)
            .commit()
    }

}
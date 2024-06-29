package com.example.viewactivitypractice.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.viewactivitypractice.MainActivity
import com.example.viewactivitypractice.R
import com.example.viewactivitypractice.adapters.PostAdapter
import com.example.viewactivitypractice.datas.PostData

/**
 * A simple [Fragment] subclass.
 * Use the [PostTab.newInstance] factory method to
 * create an instance of this fragment.
 */
class PostTab : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var postDataList: ArrayList<PostData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.post_tab_fragment, container, false)
        val postAddBtn = view.findViewById<Button>(R.id.post_upload_btn)
        postAddBtn.setOnClickListener{
            parentFragmentManager.beginTransaction().replace(R.id.blank_container, PostAddPage()).commit()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataInitialize()
        recyclerView = view.findViewById(R.id.postRecycler)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = PostAdapter(postDataList) {
            // 기능 구현 : 편잽/삭제
        }
    }

    private fun dataInitialize() {
        val postDb = (activity as MainActivity).mydb
        postDataList = postDb.getAllPost()
    }

}
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
        val postAddBtn = view.findViewById<Button>(R.id.post_edit_btn)
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
        recyclerView.adapter = PostAdapter(postDataList) { post ->
            // 클릭된 아이템의 연락처 정보를 ContactDetailPage 프래그먼트에 전달
            val detailFragment = PostDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt("POST_ID", post.id)  // 가정: ContactData에 id 필드가 있다고 가정
                    putString("POST_CONTENT", post.content)
                }
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.blank_container, detailFragment)
                .addToBackStack(null)  // Back stack을 사용하여 뒤로 가기 버튼으로 이전 화면으로 돌아갈 수 있도록 함
                .commit()
        }
    }

    private fun dataInitialize() {
        val postDb = (activity as MainActivity).mydb
        postDataList = postDb.getAllPost()
    }

    object HashTagParser {
        fun parse(content: String): List<String> {
            val regex = Regex("#(\\w+)")
            return regex.findAll(content).map { it.groupValues[1] }.toList()
        }
    }

}
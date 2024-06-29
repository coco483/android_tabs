package com.example.viewactivitypractice.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.viewactivitypractice.MainActivity
import com.example.viewactivitypractice.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PostDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PostDetailFragment : Fragment() {
    private var postId: Int = -1  // 인스턴스 변수로 ID 저장
    private lateinit var postContent: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            postId = it.getInt("POST_ID")
            postContent = it.getString("POST_CONTENT").toString()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mydb = (activity as MainActivity).mydb
        val view = inflater.inflate(R.layout.post_detail_page, container, false)
        view.findViewById<EditText>(R.id.content_ET).setText(postContent)
        val editBtn = view.findViewById<Button>(R.id.edit_btn)
        editBtn.setOnClickListener {

            val newContent = view.findViewById<EditText>(R.id.content_ET).text.toString()
            if (newContent == "") {
                Toast.makeText(requireContext(), "내용을 입력해 주세요", Toast.LENGTH_SHORT).show()
            } else {
                mydb.updatePost(postId, newContent) // 연락처 업데이트
                parentFragmentManager.beginTransaction().replace(R.id.blank_container, PostTab())
                    .commit() // 변경 사항 반영
            }
        }
        return view
    }

}
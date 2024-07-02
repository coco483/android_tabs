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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.viewactivitypractice.DataBaseHandler
import com.example.viewactivitypractice.MainActivity
import com.example.viewactivitypractice.R
import com.example.viewactivitypractice.adapters.PostAdapter
import com.example.viewactivitypractice.datas.PostData


class ContactDetailPage : Fragment() {

    private var contactId: Int = -1  // 인스턴스 변수로 ID 저장
    private lateinit var contactName: String
    private lateinit var contactPhoneNum: String
    private lateinit var myDB: DataBaseHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            contactId = it.getInt("CONTACT_ID")
            contactName = it.getString("EXTRA_CONTACT_NAME").toString()
            contactPhoneNum = it.getString("EXTRA_CONTACT_PHONE").toString()
            Log.d("ContactDetail", "id: $contactId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myDB = (activity as MainActivity).mydb
        val view =  inflater.inflate(R.layout.contact_detail_page, container, false)
        view.findViewById<EditText>(R.id.name_ET).setText(contactName)
        view.findViewById<EditText>(R.id.phoneNum_ET).setText(contactPhoneNum)

        val editBtn = view.findViewById<Button>(R.id.edit_btn)
        val deleteBtn = view.findViewById<Button>(R.id.delete_btn)

        editBtn.setOnClickListener {

            val newName = view.findViewById<EditText>(R.id.name_ET).text.toString()
            val newPhoneNum = view.findViewById<EditText>(R.id.phoneNum_ET).text.toString()
            if (newPhoneNum.length != 11) {
                Toast.makeText(requireContext(), "전화번호 11자리를 모두 입력해 주세요", Toast.LENGTH_SHORT).show()
            } else if (newName == "") {
                Toast.makeText(requireContext(), "이름을 입력해 주세요", Toast.LENGTH_SHORT).show()
            } else {
                myDB.updateContact(contactId, newName, newPhoneNum) // 연락처 업데이트
                parentFragmentManager.beginTransaction().replace(R.id.blank_container, ContactTab())
                    .commit() // 변경 사항 반영
            }
        }

        deleteBtn.setOnClickListener {
            if (contactId != -1) {
                myDB.deleteContactById(contactId) // ID를 사용하여 삭제
                Toast.makeText(context, "Contact deleted successfully", Toast.LENGTH_SHORT).show()
                parentFragmentManager.beginTransaction().replace(R.id.blank_container, ContactTab())
                    .commit()
            } else {
                Toast.makeText(context, "Invalid contact ID", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val postDataList = myDB.getPostByContactId(contactId)
        setPostRecyclerView(view, postDataList)
    }
    fun setPostRecyclerView(view: View, postDatas: ArrayList<PostData>) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.contact_tagged_post_RV)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = PostAdapter(myDB, postDatas) { post ->
            // 클릭된 아이템의 연락처 정보를 PostDetailPage 프래그먼트에 전달
            val detailPage = PostDetailPage().apply {
                arguments = Bundle().apply {
                    putInt("POST_ID", post.id)  // 가정: PostData에 id 필드가 있다고 가정
                    putString("POST_CONTENT", post.content)
                    post.imageId?.let { putInt("POST_IMG_ID", it) }
                }
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.blank_container, detailPage)
                .addToBackStack(null)  // Back stack을 사용하여 뒤로 가기 버튼으로 이전 화면으로 돌아갈 수 있도록 함
                .commit()
        }
    }


}
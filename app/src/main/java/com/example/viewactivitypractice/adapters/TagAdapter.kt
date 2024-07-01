package com.example.viewactivitypractice.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.viewactivitypractice.DataBaseHandler
import com.example.viewactivitypractice.R
import com.example.viewactivitypractice.datas.TagData

// PostTab의 태그텍스트 화면에 보인
// 각 요소를 클릭하면 연락처로 보내지는 이벤트
class TagAdapter(private val myDB: DataBaseHandler, private var tagList: ArrayList<TagData>, private val onClick: (TagData) -> Unit) :
    RecyclerView.Adapter<TagAdapter.TagViewHolder>(){

    class TagViewHolder(itemView: View, onClick: (TagData) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val tagName: TextView = itemView.findViewById(R.id.tag_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.tag_items, parent, false)
        return TagViewHolder(itemView, onClick)
    }


    override fun getItemCount(): Int {
        return tagList.size
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        var currentItem = tagList[position]
        holder.itemView.setOnClickListener { onClick(currentItem) }
        val contactNames = myDB.getContactNameList(arrayListOf(currentItem.contactId))
        holder.tagName.text = if (contactNames.isNotEmpty()) contactNames[0].toString() else "Unknown"

    }

}
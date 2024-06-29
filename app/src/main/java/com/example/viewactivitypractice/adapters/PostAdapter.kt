package com.example.viewactivitypractice.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.viewactivitypractice.R
import com.example.viewactivitypractice.datas.PostData

// 리스트를 화면에 표시하기 위해서 아이템 단위로 View로 생성을 해서 RecyclerView에 바인딩 시키는 작업을 하는 객체
// 싸그리 수정해야함.
class PostAdapter(private val postList: ArrayList<PostData>, private val onClick: (PostData) -> Unit) :
    RecyclerView.Adapter<PostAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.post_items, parent, false)
        return MyViewHolder(itemView, onClick)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var currentItem = postList[position]
        holder.itemView.setOnClickListener { onClick(currentItem) }
        holder.content.text = currentItem.content
        holder.date.text = currentItem.date
        holder.date.text = currentItem.tagsId

    }
    private fun formatPhoneNumbr(input: String): String {
        // Ensure the input string is exactly 11 digits long
        if (input.length != 11 || !input.all { it.isDigit() }) {
            throw IllegalArgumentException("Input must be a string of exactly 11 digits")
        }
        val part1 = input.substring(0, 3)
        val part2 = input.substring(3, 7)
        val part3 = input.substring(7, 11)
        return "$part1-$part2-$part3"
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    class MyViewHolder(itemView: View, onClick: (PostData) -> Unit) : RecyclerView.ViewHolder(itemView) {
        // val name: TextView = itemView.findViewById(R.id.item_name)
        val date: TextView = itemView.findViewById(R.id.item_text_date)
        val content: TextView = itemView.findViewById(R.id.item_text_content)
        val tagsId: TextView = itemView.findViewById(R.id.item_hashtag)
        val img: ImageView = itemView.findViewById(R.id.item_image_post)
    }
}
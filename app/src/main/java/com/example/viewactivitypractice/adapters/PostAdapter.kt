package com.example.viewactivitypractice.adapters

import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.viewactivitypractice.DataBaseHandler
import com.example.viewactivitypractice.MainActivity
import com.example.viewactivitypractice.R
import com.example.viewactivitypractice.datas.PostData

// 리스트를 화면에 표시하기 위해서 아이템 단위로 View로 생성을 해서 RecyclerView에 바인딩 시키는 작업을 하는 객체
// 싸그리 수정해야함.
// 뷰홀더는 각각의 리스트 아이템 뷰를 저장하고 관리하는 객체
// 그럼 뷰홀더는 태그아이디 목록들을 텍스트로 바꿔서 출력해야하네?
class PostAdapter(private val myDB: DataBaseHandler, private val postList: ArrayList<PostData>, private val onClick: (PostData) -> Unit) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.post_items, parent, false)
        return PostViewHolder(itemView, onClick)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        var currentItem = postList[position]
        holder.itemView.setOnClickListener { onClick(currentItem) }
        holder.content.text = currentItem.content
        holder.date.text = currentItem.date
        val bitImg = myDB.getImgById(currentItem.imageId)
        holder.img.setImageBitmap(bitImg)

        /*// 태그 처리
        val spannableString = SpannableString(currentItem.taggedNameList.joinToString(" ") { "@$it" })
        currentItem.taggedNameList.forEach { tagName ->
            val tagStart = spannableString.indexOf("@$tagName")
            val tagEnd = tagStart + tagName.length + 1

            spannableString.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    onTagClick(tagName)
                }
            }, tagStart, tagEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        holder.taggedName.text = spannableString
        holder.taggedName.movementMethod = LinkMovementMethod.getInstance()

        // 해시태그를 아이디(정수) -> 스트링으로 변환, 이미지를 아이디(정수) -> 이미지파일로 변환
*/

    }

    override fun getItemCount(): Int {
        return postList.size
    }

    class PostViewHolder(itemView: View, onClick: (PostData) -> Unit) : RecyclerView.ViewHolder(itemView) {
        // val name: TextView = itemView.findViewById(R.id.item_name)
        val date: TextView = itemView.findViewById(R.id.item_text_date)
        val content: TextView = itemView.findViewById(R.id.item_text_content)
        val taggedName: TextView = itemView.findViewById(R.id.item_tag_text)
        val img: ImageView = itemView.findViewById(R.id.item_image_post)
        }
    }

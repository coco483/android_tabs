package com.example.viewactivitypractice.adapters

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.viewactivitypractice.DataBaseHandler
import com.example.viewactivitypractice.R
import com.example.viewactivitypractice.datas.PostData
import com.example.viewactivitypractice.fragments.ContactDetailPage

class PostAdapter(private val myDB: DataBaseHandler, private val postList: ArrayList<PostData>, private val onClick: (PostData) -> Unit) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.post_items, parent, false)
        return PostViewHolder(itemView, onClick)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentItem = postList[position]
        holder.itemView.setOnClickListener { onClick(currentItem) }
        holder.content.text = currentItem.content
        holder.date.text = currentItem.date
        val bitImg: Bitmap? = myDB.getImgById(currentItem.imageId)
        // bitImg?.let { holder.img.setImageBitmap(it) }
        if (bitImg != null) {
            holder.img.setImageBitmap(bitImg)
            holder.img.visibility = View.VISIBLE
        } else {
            holder.img.visibility = View.GONE
        }

        val contactIdSet : Set<Int> =  myDB.getAllContactIdByPostId(currentItem.id)
        Log.d("method check", "${myDB.getAllContactIdByPostId(currentItem.id)}")
        Log.d("postId check", "$currentItem.id: ${currentItem.id}")
        Log.d("contactIdSet Check", "$contactIdSet")
        val spannable = SpannableStringBuilder()

        for (contactId in contactIdSet) {

            Log.d("loop", "click contact event id:${contactId}")
            val start = spannable.length
            val contact = myDB.getContactByContactId(contactId)
            val contactName = contact?.name

            if (contactName != null) {
                spannable.append("@$contactName")
                Log.d("spannable", "id: $contactId name: $contactName")

                // Apply styles
                spannable.setSpan(
                    StyleSpan(Typeface.ITALIC),
                    start,
                    spannable.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.setSpan(
                    UnderlineSpan(),
                    start,
                    spannable.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                // Apply ClickableSpan
                spannable.setSpan(
                    object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            val context = widget.context
                            if (context is FragmentActivity) {
                                    val contactDetailFragment = ContactDetailPage().apply {
                                        arguments = Bundle().apply {
                                            putInt("CONTACT_ID", contact.id)
                                            putString("EXTRA_CONTACT_NAME", contact.name)
                                            putString("EXTRA_CONTACT_PHONE", contact.phonenumber)
                                        }
                                    }
                                    context.supportFragmentManager.beginTransaction()
                                        .replace(R.id.blank_container, contactDetailFragment)
                                        .addToBackStack(null)
                                        .commit()
                            } // fragment 검사
                        } // onClick
                    },
                    start,
                    spannable.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                ) // setSpan
                Log.d("checkClickStart", "start: $start")

                spannable.append(", ")
            } // 널검사
        } // loop end

        if (spannable.isNotEmpty()) {
            spannable.delete(spannable.length - 2, spannable.length)
        }

        Log.d("spannable check", "current spannable: $spannable")
        holder.taggedName.text = spannable
        Log.d("spannable viewing check", "current spannable: $spannable")
        holder.taggedName.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    class PostViewHolder(itemView: View, onClick: (PostData) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.item_text_date)
        val content: TextView = itemView.findViewById(R.id.item_text_content)
        val taggedName: TextView = itemView.findViewById(R.id.item_tag_text)
        val img: ImageView = itemView.findViewById(R.id.item_image_post)
    }
}

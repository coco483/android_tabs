package com.example.viewactivitypractice.adapters

import android.graphics.Bitmap
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
        bitImg?.let { holder.img.setImageBitmap(it) }

        val tagNameArray: ArrayList<String> = myDB.getContactNameArrayList(currentItem.tagIdList)
        val tagIdArray: ArrayList<Int> = currentItem.tagIdList

        val spannable = SpannableStringBuilder()

        for ((index, tagName) in tagNameArray.withIndex()) {

            Log.d("loop", "click tag event${index} ${tagName}")
            val tagId = tagIdArray[index]
            val start = spannable.length
            spannable.append(tagName)
            Log.d("spannable", "start: $start spannable: $spannable")

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
                            val contact = myDB.getContactByTagId(tagId)
                            if (contact != null) {
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
                            } else {
                                Toast.makeText(context, "Contact not found", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                start,
                spannable.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            Log.d("checkClickStart", "start: $start")

            spannable.append(", ")
        }

        if (spannable.isNotEmpty()) {
            spannable.delete(spannable.length - 2, spannable.length)
        }

        holder.taggedName.text = spannable
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

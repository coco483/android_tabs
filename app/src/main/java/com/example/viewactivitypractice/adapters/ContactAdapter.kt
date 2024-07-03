package com.example.viewactivitypractice.adapters

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.viewactivitypractice.R
import com.example.viewactivitypractice.datas.ContactData

class ContactAdapter(private val numberList: ArrayList<ContactData>,
                     private val editClick: (ContactData) -> Unit,
                     private val deleteClick: (Int) -> Unit) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        var itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.contact_items, parent, false)
        return ContactViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        var currentItem = numberList[position]
        holder.itemView.setOnClickListener{
            holder.detailLayout.visibility = if (holder.detailLayout.isVisible) View.GONE else View.VISIBLE
            holder.phoneNum.text = formatPhoneNumber(currentItem.phonenumber)
            holder.editButton.setOnClickListener { editClick(currentItem) }
            holder.deleteButton.setOnClickListener { deleteClick(currentItem.id) }
        }
        holder.name.text = currentItem.name

    }
    private fun formatPhoneNumber(input: String): String {
        // Ensure the input string is exactly 11 digits long
        if (input.length != 11 || !input.all { it.isDigit() }) {
            throw IllegalArgumentException("Input must be a string of exactly 11 digits")
        }
        val part1 = input.substring(0, 3)
        val part2 = input.substring(3, 7)
        val part3 = input.substring(7, 11)
        return "휴대전화 $part1-$part2-$part3"
    }

    override fun getItemCount(): Int {
        return numberList.size
    }

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.item_name)
        val phoneNum: TextView = itemView.findViewById(R.id.item_num)
        val detailLayout = itemView.findViewById<LinearLayout>(R.id.contact_detail_layout)
        val deleteButton = itemView.findViewById<Button>(R.id.contact_delete_button)
        val editButton = itemView.findViewById<Button>(R.id.contact_edit_button)
    }


}
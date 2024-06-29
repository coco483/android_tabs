package com.example.viewactivitypractice.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.viewactivitypractice.R
import com.example.viewactivitypractice.datas.ContactData

class ContactAdapter(private val numberList: ArrayList<ContactData>, private val onClick: (ContactData) -> Unit) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        var itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.number_items, parent, false)
        return ContactViewHolder(itemView, onClick)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        var currentItem = numberList[position]
        holder.itemView.setOnClickListener { onClick(currentItem) }
        holder.name.text = currentItem.name
        holder.phoneNum.text = formatPhoneNumber(currentItem.phonenumber)
    }
    fun formatPhoneNumber(input: String): String {
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
        return numberList.size
    }

    class ContactViewHolder(itemView: View, onClick: (ContactData) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.item_name)
        val phoneNum: TextView = itemView.findViewById(R.id.item_num)
    }


}
package com.example.viewactivitypractice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val numberList: ArrayList<NumberDatas>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.number_items, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = numberList[position]
        holder.phoneName.text = currentItem.contactname
        holder.phoneNum.text = currentItem.number
    }

    override fun getItemCount(): Int {
        return numberList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val phoneName : TextView = itemView.findViewById(R.id.item_name)
        val phoneNum : TextView = itemView.findViewById(R.id.item_num)
    }
}
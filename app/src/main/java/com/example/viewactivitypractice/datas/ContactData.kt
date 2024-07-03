package com.example.viewactivitypractice.datas

class ContactData(
    val id : Int,
    var name : String,
    var phonenumber: String,
    val imageId: Int? = null
    ) {
    override fun toString(): String = this.name
}
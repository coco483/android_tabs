package com.example.viewactivitypractice.datas

data class PostData (
    var id : Int,
    var content: String,
    var date: String,
    val tagIdList: ArrayList<Int> = arrayListOf<Int>(),
    val imageId: Int? = null
)
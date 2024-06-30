package com.example.viewactivitypractice.datas

data class PostData (
    var id : Int,
    var content: String,
    var date: String,
    val taggedNameList: List<String> = emptyList(),
    val imageId: Int? = null
)
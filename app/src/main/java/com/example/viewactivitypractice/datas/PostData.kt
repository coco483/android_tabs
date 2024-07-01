package com.example.viewactivitypractice.datas

data class PostData (
    var id : Int,
    var content: String,
    var date: String,
    val tagIdList: List<Int> = emptyList(),
    val imageId: Int? = null
)
package com.example.viewactivitypractice.datas

data class PostData (
    var id : Int,
    var content: String,
    var date: String,
    // val tagsId: List<Int> = emptyList(),
    val imageId: Int? = 0
)
package com.example.viewactivitypractice.datas

data class PostData (
    var id : Int,
    var content: String,
    var date: String,
    val imageId: Int? = null
)
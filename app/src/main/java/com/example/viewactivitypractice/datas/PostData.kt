package com.example.viewactivitypractice.datas

data class PostData (
    val id : Int,
    var content: String,
    var date: String,
    var imageId: Int? = null
)
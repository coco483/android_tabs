package com.example.viewactivitypractice.datas

data class PostData (
    var id : Int = 0,
    var content: String = "",
    var date: String = "",
    val tagsId: List<Int> = ,
    val imageId: Int? = 0
    // val image: 추후 구현
)
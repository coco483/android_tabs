package com.example.viewactivitypractice.datas

data class PostData (
    var id : Int,
    var content: String,
    var date: String,
    var tagsId: List<Int> = emptyList(),
    var imageId: Int? = 0
    // val image: 추후 구현
)
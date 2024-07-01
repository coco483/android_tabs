package com.example.viewactivitypractice.datas

import android.graphics.Bitmap
import android.net.Uri

class ImageData() {
    var id :Int = 0
    lateinit var imgPath : String

    constructor(id:Int, imgPath: String) : this() {
        this.id = id
        this.imgPath = imgPath
    }
}
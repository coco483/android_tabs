package com.example.viewactivitypractice.datas

import android.graphics.Bitmap
import android.net.Uri

class ImageData() {
    var id :Int = 0
    lateinit var img : Bitmap

    constructor(id:Int, img: Bitmap) : this() {
        this.id = id
        this.img = img
    }
}
package com.example.viewactivitypractice.datas

import android.graphics.Bitmap

class ImageData() {
    var id :Int = 0
    var img : Bitmap? = null
    constructor(id:Int, img: Bitmap?) : this() {
        this.id = id
        this.img = img
    }
}
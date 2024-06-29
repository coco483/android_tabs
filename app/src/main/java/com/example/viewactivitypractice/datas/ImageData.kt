package com.example.viewactivitypractice.datas

import android.graphics.Bitmap
import android.net.Uri

class ImageData() {
    var id :Int = 0
    var img : Uri? = null
    constructor(id:Int, img: Uri?) : this() {
        this.id = id
        this.img = img
    }
}
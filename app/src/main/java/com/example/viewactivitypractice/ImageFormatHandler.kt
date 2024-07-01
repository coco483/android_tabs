package com.example.viewactivitypractice

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.fragment.app.FragmentActivity
import java.io.ByteArrayOutputStream
import java.io.InputStream

// bitmap to BLOB
fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 10, stream)
    return stream.toByteArray()
}
// BLOB to bitmap
fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}
// URI to bitmap
// fragment안에서 activity는 requireActivity() 쓰면 된다
fun uriToBitmap(uri: Uri?, activity: FragmentActivity): Bitmap? {
    if (uri == null) return null
    val contentResolver = activity.contentResolver
    val inputStream: InputStream? = contentResolver.openInputStream(uri)
    return BitmapFactory.decodeStream(inputStream)
}
package com.example.viewactivitypractice

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.fragment.app.FragmentActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
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

fun saveBitmapToInternalStorage(context: Context, bitmap: Bitmap): String? {
    // auto increments file name
    val sharedPreferences = context.getSharedPreferences("bitmap_prefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    val currentCounter = sharedPreferences.getInt("file_counter", 0)
    val newCounter = currentCounter + 1
    editor.putInt("file_counter", newCounter)
    editor.apply()
    val fileName = "image_$newCounter.png"

    val directory = context.filesDir
    val file = File(directory, fileName)

    var fileOutputStream: FileOutputStream? = null
    return try {
        fileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        file.absolutePath
    } catch (e: IOException) {
        e.printStackTrace()
        null
    } finally {

        fileOutputStream?.close()
    }
}


fun getBitmapFromPath(filePath: String): Bitmap? {
    return BitmapFactory.decodeFile(filePath)
}
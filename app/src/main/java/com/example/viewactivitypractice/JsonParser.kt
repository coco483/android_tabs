package com.example.viewactivitypractice

import android.content.Context
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson

fun readJsonFromAssets(context: Context, fileName: String): String {
    return context.assets.open(fileName).bufferedReader().use { it.readText() }
}

fun parseJsonToNumberDatas(jsonString: String): ArrayList<NumberDatas> {
    val gson = Gson()
    return gson.fromJson(jsonString, object : TypeToken<ArrayList<NumberDatas>>() {}.type)
}
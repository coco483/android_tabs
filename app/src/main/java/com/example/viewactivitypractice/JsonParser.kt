package com.example.viewactivitypractice

import android.content.Context
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson

// src/main/assets에서 json 파일 string으로 읽어오기
fun readJsonFromAssets(context: Context, fileName: String): String {
    return context.assets.open(fileName).bufferedReader().use { it.readText() }
}

// string형태의 json array를 ArrayList<ContactData>로 변환
fun parseJsonToNumberDatas(jsonString: String): ArrayList<ContactData> {
    val gson = Gson()
    return  gson.fromJson(jsonString, object : TypeToken<ArrayList<ContactData>>(){}.type)
}
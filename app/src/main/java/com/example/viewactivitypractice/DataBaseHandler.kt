package com.example.viewactivitypractice

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.viewactivitypractice.datas.ContactData

val DATABASE_NAME = "MyDB"
val CONTACT_TABLE_NAME = "Contacts"
val COL_NAME = "name"
val COL_ID = "id"
val COL_PHONENUM = "phonenumber"

class DataBaseHandler(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, 1){
    override fun onCreate(db: SQLiteDatabase?) {
        Log.d("DBhandler", "db create table")
        val createTableQuery = ("CREATE TABLE $CONTACT_TABLE_NAME ("+
                "$COL_ID INTEGER PRIMARY KEY  AUTOINCREMENT, "+
                "$COL_NAME VARCHAR, "+
                "$COL_PHONENUM VARCHAR(11))")
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldeversion: Int, newversion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $CONTACT_TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }
    fun insertContact(name:String, phonenumber:String): Long {
        val values = ContentValues().apply {
            put(COL_NAME, name)
            put(COL_PHONENUM, phonenumber)
        }
        val db = writableDatabase
        return db.insert(CONTACT_TABLE_NAME, null, values)
    }
    fun getAllContact(): ArrayList<ContactData>{
        val contactList = ArrayList<ContactData>()
        val db = readableDatabase
        val query = ("SELECT * FROM $CONTACT_TABLE_NAME")
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME))
                val phonenumber = cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONENUM))
                val contact = ContactData(name, phonenumber)
                contactList.add(contact)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return contactList
    }
}
package com.example.viewactivitypractice

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.viewactivitypractice.datas.ContactData
import com.example.viewactivitypractice.datas.PostData


private const val DATABASE_NAME = "MyDB"

// for Contact
private const val CONTACT_TABLE_NAME = "Contacts"
private const val COL_NAME = "name"
private const val COL_ID = "id"
private const val COL_PHONENUM = "phonenumber"

// for Gallery

// for Post
private const val POST_TABLE_NAME = "Post"
private const val COL_POST_ID = "post_id"
private const val COL_CONTENT = "main_text"
private const val COL_DATE = "date"
private const val COL_TAGGED_ID = "tagged_id"
private const val COL_IMAGE_ID = "image_id"

class DataBaseHandler(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, 1){
    override fun onCreate(db: SQLiteDatabase) {
        createContactTable(db)
        createPostTable(db)
    }

    // 컨택트 테이블
    private fun createContactTable(db: SQLiteDatabase?) {
        Log.d("ContactDBhandler", "contact db create table")
        val createContactTableQuery = ("CREATE TABLE $CONTACT_TABLE_NAME (" +
                "$COL_ID INTEGER PRIMARY KEY  AUTOINCREMENT, " +
                "$COL_NAME VARCHAR, " +
                "$COL_PHONENUM VARCHAR(11))")
        db?.execSQL(createContactTableQuery)
    }

    // 포스트 테이블 생성
    private fun createPostTable(db: SQLiteDatabase?) {
        Log.d("PostDBhandler", "post db create table")
        val createPostTableQuery = """
            CREATE TABLE $POST_TABLE_NAME (
                $COL_POST_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_CONTENT TEXT,
                $COL_DATE TEXT,
                $COL_TAGGED_ID TEXT,
                $COL_IMAGE_ID INTEGER
            );
        """.trimIndent()
        db?.execSQL(createPostTableQuery)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldeversion: Int, newversion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $CONTACT_TABLE_NAME"
        db?.execSQL(dropTableQuery)
        db?.let {
            onCreate(it)
        }
    }

    // CRUD
    // Contant
    // 만들기
    fun insertContact(name:String, phonenumber:String): Long {
        Log.d("DBhandler", "insert contact $name, $phonenumber")
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
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME))
                val phonenumber = cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONENUM))
                val contact = ContactData(id, name, phonenumber)
                contactList.add(contact)
                Log.d("DBread", "read $name, $phonenumber")
            } while (cursor.moveToNext())
        }
        cursor.close()
        return contactList
    }

    // 연락처 삭제
    fun deleteContactById(contactId: Int) {
        val db = this.writableDatabase
        db.delete(CONTACT_TABLE_NAME, "id = ?", arrayOf(contactId.toString()))
        db.close()
    }

    // 연락처 편집
    fun updateContact(id: Int, name: String, phoneNumber: String) {
        val db = this.writableDatabase
        Log.d("updateDB", "id: $id, name: $name, num: $phoneNumber")
        val contentValues = ContentValues()
        // contentValues.put(COL_ID, id)
        contentValues.put(COL_NAME, name)
        contentValues.put(COL_PHONENUM, phoneNumber)

        db.update(CONTACT_TABLE_NAME, contentValues, "id = ?", arrayOf(id.toString()))
        db.close()
    }


    // 포스트
    fun getAllPost() :ArrayList<PostData>{

        val postList = ArrayList<PostData>()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $POST_TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val postId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_POST_ID))
                val content = cursor.getString(cursor.getColumnIndexOrThrow(COL_CONTENT))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE))
                // val taggedId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_TAGGED_ID)) -> 리스트로 받아올 수 있도록
                val imageId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_IMAGE_ID))

                val post = PostData(postId, content, date, tagsId = emptyList(), imageId)
                postList.add(post)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return postList
    }

    // id기반 포스트 읽기
    // 포스트 만들기
    // 포스트 업데이트
    // 포스트 삭제
}
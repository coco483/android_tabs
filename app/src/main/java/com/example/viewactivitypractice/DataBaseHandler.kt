package com.example.viewactivitypractice

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.viewactivitypractice.datas.ContactData
import com.example.viewactivitypractice.datas.ImageData
import com.example.viewactivitypractice.datas.PostData
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val DATABASE_NAME = "MyDB"

// for Contact
private const val CONTACT_TABLE_NAME = "Contacts"
private const val COL_NAME = "name"
private const val COL_CONTACT_ID = "id"
private const val COL_PHONENUM = "phonenumber"

// for Gallery
private const val IMG_TABLE_NAME = "Images"
private const val COL_IMG_ID = "id"
private const val COL_IMG = "image"
// for Post
private const val POST_TABLE_NAME = "Posts"
private const val COL_POST_ID = "id"
private const val COL_CONTENT = "content"
private const val COL_DATE = "date"
private const val COL_TAG_LIST = "tag_list"
private const val COL_IMAGE_ID = "image_id"
// for Contact-Post Table
private const val POST_TAGS_TABLE_NAME = "PostTags"
private const val COL_POSTTAG_ID = "posttag_id"
private const val COL_TAG_POST_ID = "post_id"
private const val COL_TAG_CONTACT_ID = "contact_id"

class DataBaseHandler(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, 1){
    override fun onCreate(db: SQLiteDatabase) {
        createContactTable(db)
        createImgTable(db)
        createPostTable(db)
    }

    // 컨택트 테이블 생성
    private fun createContactTable(db: SQLiteDatabase?) {
        Log.d("ContactDBhandler", "contact db create table")
        val createContactTableQuery = ("CREATE TABLE $CONTACT_TABLE_NAME (" +
                "$COL_CONTACT_ID INTEGER PRIMARY KEY  AUTOINCREMENT, " +
                "$COL_NAME VARCHAR, " +
                "$COL_PHONENUM VARCHAR(11))")
        db!!.execSQL(createContactTableQuery)
    }
    // 이미지 테이블 생성
    private fun createImgTable(db: SQLiteDatabase?){
        Log.d("ImgDBhandler", "img db create table")
        val createImgTableQuery = ("CREATE TABLE $IMG_TABLE_NAME (" +
                "$COL_IMG_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_IMG BLOB)")
        db!!.execSQL(createImgTableQuery)
    }

    // 포스트 테이블 생성
    private fun createPostTable(db: SQLiteDatabase?) {
        Log.d("PostDBhandler", "post db create table")
        val createPostTableQuery = """
            CREATE TABLE $POST_TABLE_NAME (
                $COL_POST_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_CONTENT TEXT,
                $COL_DATE TEXT,
                $COL_TAG_LIST TEXT,
                $COL_IMAGE_ID INTEGER
            );
        """.trimIndent()
        db!!.execSQL(createPostTableQuery)
    }

    // 컨택트-포스트 중간 테이블 생성
    private fun createPostTagsTable(db: SQLiteDatabase) {
        Log.d("PostTagDBhandler", "post db create table")
        val CREATE_POST_TAGS_TABLE = """
            CREATE TABLE $POST_TAGS_TABLE_NAME (
                $COL_POSTTAG_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_TAG_POST_ID INTEGER,
                $COL_TAG_CONTACT_ID INTEGER,
                FOREIGN KEY ($COL_TAG_POST_ID) REFERENCES Posts(post_id),
                FOREIGN KEY ($COL_TAG_CONTACT_ID) REFERENCES Contacts(contact_id)
            )
        """.trimIndent()
        db.execSQL(CREATE_POST_TAGS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldeversion: Int, newversion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $CONTACT_TABLE_NAME"
        db?.execSQL(dropTableQuery)
        db?.let {
            onCreate(it)
        }
    }

    // Contact
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
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CONTACT_ID))
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

    // 이미지 추가하기
    fun insertImg(img: Bitmap?): Long {
        if (img == null) return 0
        Log.d("ImgDBhandler", "insert image")
        val values = ContentValues()
        values.put(COL_IMG, bitmapToByteArray(img))
        val db = writableDatabase
        return db.insert(IMG_TABLE_NAME, null, values)
    }
    fun getAllImg(): ArrayList<ImageData>{
        val imgDataList = ArrayList<ImageData>()
        val db = readableDatabase
        val query = ("SELECT * FROM $IMG_TABLE_NAME")
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_IMG_ID))
                Log.d("ImgDBhanlder", "read $id image!")
                val img = cursor.getBlob(cursor.getColumnIndexOrThrow(COL_IMG))
                val imgData = ImageData(id, byteArrayToBitmap(img))
                imgDataList.add(imgData)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return imgDataList
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun insertPost(post: PostData): Long{
        Log.d("PostDBHandler", "insert contact ${post.content}")
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val current = LocalDateTime.now().format(formatter)
        val values = ContentValues().apply {
            put(COL_CONTENT, post.content)
            put(COL_DATE, current)
        }
        val db = writableDatabase
        return db.insert(POST_TABLE_NAME, null, values)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updatePost(post:PostData) {
        val db = this.writableDatabase
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val current = LocalDateTime.now().format(formatter)
        val postValues = ContentValues()
        postValues.put(COL_CONTENT, post.content)
        postValues.put(COL_DATE, current)

        db.update(POST_TABLE_NAME, postValues, "id = ?", arrayOf(post.id.toString()))
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
                // val taggedId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_TAG_LIST)) -> 리스트로 받아올 수 있도록
                val imageId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_IMAGE_ID))

                val post = PostData(postId, content, date, tagsId = emptyList(), imageId)
                postList.add(post)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return postList
    }

    fun deletePostById(postId: Int) {

        val db = this.writableDatabase
        db.delete(POST_TABLE_NAME, "id = ?", arrayOf(postId.toString()))
        db.close()

    }

    // 태그를 하면 연락처의 창을 열어줌
    fun getContactIdByName(contactName: String): Int? {
        val db = this.readableDatabase
        val query = "SELECT $COL_CONTACT_ID FROM $CONTACT_TABLE_NAME WHERE $COL_NAME = ?"
        val cursor = db.rawQuery(query, arrayOf(contactName))
        var contactId: Int? = null
        val columnIndex = cursor.getColumnIndex(COL_CONTACT_ID)
        if (columnIndex != -1 && cursor.moveToFirst()) {
            contactId = cursor.getInt(columnIndex)
        }
        cursor.close()
        db.close()
        return contactId
    }

    // C 태그 테이블에 새 레코드 삽입
    fun insertTag(postId: Int, userId: Int): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_TAG_POST_ID, postId)
            put(COL_TAG_CONTACT_ID, userId)
        }
        val result = db.insert(POST_TAGS_TABLE_NAME, null, values)
        db.close()
        return result
    }


    //R

    //U

    //D
    fun deleteTag(postTagId: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(POST_TAGS_TABLE_NAME, "$COL_POSTTAG_ID = ?", arrayOf(postTagId.toString()))
        db.close()
        return result
    }
}
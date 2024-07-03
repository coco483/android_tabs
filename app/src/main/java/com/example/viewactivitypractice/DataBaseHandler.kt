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
private const val CONTACT_NAME = "name"
private const val CONTACT_ID = "id"
private const val CONTACT_PHONENUM = "phonenumber"
private const val CONTACT_IMG_ID = "image_id"

// for Gallery
private const val IMG_TABLE_NAME = "Images"
private const val IMG_ID = "id"
private const val IMG_PATH = "image_path"
// for Post
private const val POST_TABLE_NAME = "Posts"
private const val POST_ID = "id"
private const val POST_CONTENT = "content"
private const val POST_DATE = "date"
private const val POST_IMG_ID = "image_id"
// for Contact-Post Table
private const val POST_TAGS_TABLE_NAME = "PostTags"
private const val POSTTAG_ID = "posttag_id"
private const val POSTTAG_POST_ID = "post_id"
private const val POSTTAG_CONTACT_ID = "contact_id"

class DataBaseHandler(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, 1){
    override fun onCreate(db: SQLiteDatabase) {
        createContactTable(db)
        createImgTable(db)
        createPostTable(db)
        createPostTagsTable(db)
    }

    // 컨택트 테이블 생성
    private fun createContactTable(db: SQLiteDatabase?) {
        Log.d("ContactDBhandler", "contact db create table")
        val createContactTableQuery = ("CREATE TABLE $CONTACT_TABLE_NAME (" +
                "$CONTACT_ID INTEGER PRIMARY KEY  AUTOINCREMENT, " +
                "$CONTACT_NAME VARCHAR, " +
                "$CONTACT_PHONENUM VARCHAR(11), "+
                "$CONTACT_IMG_ID INTEGER)")
        db!!.execSQL(createContactTableQuery)
    }
    // 이미지 테이블 생성
    private fun createImgTable(db: SQLiteDatabase?){
        Log.d("ImgDBhandler", "img db create table")
        val createImgTableQuery = ("CREATE TABLE $IMG_TABLE_NAME (" +
                "$IMG_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$IMG_PATH VARCHAR)")
        db!!.execSQL(createImgTableQuery)
    }

    // 포스트 테이블 생성
    private fun createPostTable(db: SQLiteDatabase?) {
        Log.d("PostDBhandler", "post db create table")
        val createPostTableQuery = """
            CREATE TABLE $POST_TABLE_NAME (
                $POST_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $POST_CONTENT TEXT,
                $POST_DATE TEXT,
                $POST_IMG_ID INTEGER
            );
        """.trimIndent()
        db!!.execSQL(createPostTableQuery)
    }

    // 컨택트-포스트 중간 테이블 생성
    private fun createPostTagsTable(db: SQLiteDatabase) {
        Log.d("PostTagDBhandler", "post db create table")
        val CREATE_POST_TAGS_TABLE = """
            CREATE TABLE $POST_TAGS_TABLE_NAME (
                $POSTTAG_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $POSTTAG_POST_ID INTEGER,
                $POSTTAG_CONTACT_ID INTEGER,
                FOREIGN KEY ($POSTTAG_POST_ID) REFERENCES $POST_TABLE_NAME($POST_ID),
                FOREIGN KEY ($POSTTAG_CONTACT_ID) REFERENCES $CONTACT_TABLE_NAME($CONTACT_ID)
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
    fun insertContact(contact: ContactData): Long {
        Log.d("DBhandler", "insert contact ${contact.name}, ${contact.phonenumber}")
        val values = ContentValues().apply {
            put(CONTACT_NAME, contact.name)
            put(CONTACT_PHONENUM, contact.phonenumber)
            put(CONTACT_IMG_ID, contact.imageId)
        }
        val db = writableDatabase
        return db.insert(CONTACT_TABLE_NAME, null, values)
    }
    fun getContactIncludes(subStr:String): ArrayList<ContactData> {
        val contactList = ArrayList<ContactData>()
        val db = readableDatabase
        val query = ("SELECT * FROM $CONTACT_TABLE_NAME WHERE $CONTACT_NAME LIKE ? OR $CONTACT_PHONENUM LIKE ?")
        val cursor = db.rawQuery(query, arrayOf("%$subStr%", "%$subStr%"))
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(CONTACT_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(CONTACT_NAME))
                val phonenumber = cursor.getString(cursor.getColumnIndexOrThrow(CONTACT_PHONENUM))
                val contact = ContactData(id, name, phonenumber)
                contactList.add(contact)
                Log.d("ContactDB", "searched $name, $phonenumber")
            } while (cursor.moveToNext())
        }
        cursor.close()
        return contactList
    }
    fun getContactNameList(contactIdList: ArrayList<Int>): String{
        var contactNameList = arrayListOf<String>()
        val db = readableDatabase
        val query = ("SELECT * FROM $CONTACT_TABLE_NAME WHERE $CONTACT_ID = ?")
        for (contactId in contactIdList){
            val cursor = db.rawQuery(query, arrayOf(contactId.toString()))
            if (cursor.moveToFirst()) {
                val name = cursor.getString(cursor.getColumnIndexOrThrow(CONTACT_NAME))
                contactNameList.add(name)
            }
            cursor.close()
        }
        db.close()
        return contactNameList.joinToString(separator = ", ") { "@$it" }
    }
    fun getContactNameArrayList(contactIdList: ArrayList<Int>): ArrayList<String>{
        var contactNameList = arrayListOf<String>()
        val db = readableDatabase
        val query = ("SELECT * FROM $CONTACT_TABLE_NAME WHERE $CONTACT_ID = ?")
        for (contactId in contactIdList){
            val cursor = db.rawQuery(query, arrayOf(contactId.toString()))
            if (cursor.moveToFirst()) {
                val name = cursor.getString(cursor.getColumnIndexOrThrow(CONTACT_NAME))
                contactNameList.add(name)
            }
            cursor.close()
        }
        db.close()
        return contactNameList
    }

    fun getAllContact(): ArrayList<ContactData>{
        val contactList = ArrayList<ContactData>()
        val db = readableDatabase
        val query = ("SELECT * FROM $CONTACT_TABLE_NAME")
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(CONTACT_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(CONTACT_NAME))
                val phonenumber = cursor.getString(cursor.getColumnIndexOrThrow(CONTACT_PHONENUM))
                val imageId = cursor.getInt(cursor.getColumnIndexOrThrow(CONTACT_IMG_ID))
                val contact = ContactData(id, name, phonenumber, imageId)
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
    fun updateContact(contact: ContactData) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        // contentValues.put(COL_ID, id)
        contentValues.put(CONTACT_NAME, contact.name)
        contentValues.put(CONTACT_PHONENUM, contact.phonenumber)
        contentValues.put(CONTACT_IMG_ID, contact.imageId)

        db.update(CONTACT_TABLE_NAME, contentValues, "id = ?", arrayOf(contact.id.toString()))
        db.close()
    }

    // 이미지 추가하기
    fun insertImg(imgPath: String): Long {
        val values = ContentValues().apply {
            put(IMG_PATH, imgPath)
        }
        val db = writableDatabase
        val imgId = db.insert(IMG_TABLE_NAME, null, values)
        db.close()
        return imgId
    }

    fun deleteImgById(imgId: Int) {
        val db = this.writableDatabase
        db.delete(IMG_TABLE_NAME, "id = ?", arrayOf(imgId.toString()))
        db.close()
    }

    fun getImgById(id: Int?): Bitmap?{
        if (id==null) return null
        val db = readableDatabase
        val query = ("SELECT $IMG_PATH FROM $IMG_TABLE_NAME WHERE $IMG_ID = ?")
        val cursor = db.rawQuery(query, arrayOf(id.toString()))
        var imgPath: String? = null
        if (cursor.moveToFirst()) {
            imgPath = cursor.getString(cursor.getColumnIndexOrThrow(IMG_PATH))
        }
        cursor.close()
        db.close()
        if (imgPath == null) return null
        return getBitmapFromPath(imgPath)
    }
    fun getAllImg(): ArrayList<ImageData>{
        val imgDataList = ArrayList<ImageData>()
        val db = readableDatabase
        val query = ("SELECT * FROM $IMG_TABLE_NAME")
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(IMG_ID))
                Log.d("ImgDBhanlder", "read $id image!")
                val imgPath = cursor.getString(cursor.getColumnIndexOrThrow(IMG_PATH))
                val imgData = ImageData(id, imgPath)
                imgDataList.add(imgData)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return imgDataList
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun insertPost(post: PostData, tagIdSet: Set<Int>): Long{
        Log.d("PostDBHandler", "insert post ${post.content}, ${post.imageId}")
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val current = LocalDateTime.now().format(formatter)
        val values = ContentValues().apply {
            put(POST_CONTENT, post.content)
            put(POST_DATE, current)
            put(POST_IMG_ID, post.imageId)
        }
        val db = writableDatabase
        val postId = db.insert(POST_TABLE_NAME, null, values)
        // insert tags in TAG_TABLE_NAME
        if (postId != -1L) {
            for (tagId in tagIdSet) insertTag(postId.toInt(), tagId)
        } else Log.e("PostDBHandler", "Failed to insert post")

        return postId
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updatePost(post:PostData, tagIdSet: Set<Int>) {
        val db = this.writableDatabase
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val current = LocalDateTime.now().format(formatter)

        val postValues = ContentValues()
        postValues.put(POST_CONTENT, post.content)
        postValues.put(POST_DATE, current)
        post.imageId?.let {
            postValues.put(POST_IMG_ID, it)
        }
        db.update(POST_TABLE_NAME, postValues, "id = ?", arrayOf(post.id.toString()))
        deleteTagByPostId(post.id)
        for (tagId in tagIdSet) insertTag(post.id, tagId)
        //postValues.put(POST_IMG_ID, post.imageId)
        db.close()
        // for (contactId in post.tagIdList) insertTag(post.id, contactId)
    }
    // 포스트 삭제
    fun deletePostById(postId: Int) {
        val readableDB = readableDatabase
        val query = ("SELECT $POST_IMG_ID FROM $POST_TABLE_NAME WHERE $POST_ID = ?")
        val cursor = readableDB.rawQuery(query, arrayOf(postId.toString()))
        val postImgId: Int
        if (cursor.moveToFirst()) {
            postImgId = cursor.getInt(cursor.getColumnIndexOrThrow(POST_IMG_ID))
            deleteImgById(postImgId)
        }
        cursor.close()
        readableDB.close()
        val writableDB = this.writableDatabase
        writableDB.delete(POST_TABLE_NAME, "id = ?", arrayOf(postId.toString()))
        writableDB.close()
        deleteTagByPostId(postId)
    }
    // 포스트 조회
    private fun getPostIdByContactId(contactId: Int): ArrayList<Int>{
        val postIdList = ArrayList<Int>()
        val db = this.readableDatabase
        val query = ("SELECT * FROM $POST_TAGS_TABLE_NAME WHERE $POSTTAG_CONTACT_ID LIKE ?")
        val cursor = db.rawQuery(query, arrayOf("$contactId"))
        if (cursor.moveToFirst()) {
            do {
                val postId = cursor.getInt(cursor.getColumnIndexOrThrow(POSTTAG_POST_ID))
                postIdList.add(postId)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return postIdList
    }

    // 이미지-포스트 연결
    fun getPostByImageId(imageId: Int): PostData? {
        val db = readableDatabase
        val query = "SELECT * FROM $POST_TABLE_NAME WHERE $POST_IMG_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(imageId.toString()))
        var postData: PostData? = null

        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(POST_ID))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(POST_CONTENT))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(POST_DATE))
            postData = PostData(id, content, date, imageId)
        }

        cursor.close()
        db.close()
        return postData
    }



    fun getPostByContactId(contactId: Int): ArrayList<PostData>{
        val postList = ArrayList<PostData>()
        val postIdList = getPostIdByContactId(contactId)

        val query = ("SELECT * FROM $POST_TABLE_NAME WHERE $POST_ID = ?")
        for (postId in postIdList){
            val db = this.readableDatabase
            val cursor = db.rawQuery(query, arrayOf(postId.toString()))
            if (cursor.moveToFirst()) {
                val content = cursor.getString(cursor.getColumnIndexOrThrow(POST_CONTENT))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(POST_DATE))
                // val tagList = getAllTagByPostId(postId)
                val imageId = cursor.getInt(cursor.getColumnIndexOrThrow(POST_IMG_ID))
                val post = PostData(postId, content, date,/*tagList, */imageId)
                postList.add(post)
            }
            cursor.close()
            db.close()
        }
        return postList
    }

    // 포스트 검색
    fun getPostIncludes(subStr:String): ArrayList<PostData> {
        val postList = ArrayList<PostData>()
        val db = readableDatabase
        val query = ("SELECT * FROM $POST_TABLE_NAME WHERE $POST_CONTENT LIKE ?")
        val cursor = db.rawQuery(query, arrayOf("%$subStr%"))
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(POST_ID))
                val content = cursor.getString(cursor.getColumnIndexOrThrow(POST_CONTENT))
                val date = cursor . getString (cursor.getColumnIndexOrThrow(POST_DATE))
                val img = cursor.getInt(cursor.getColumnIndexOrThrow(POST_IMG_ID))
                val post = PostData(id, content, date, img)
                postList.add(post)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return postList
    }
    // 포스트
    fun getAllPost() :ArrayList<PostData>{

        val postList = ArrayList<PostData>()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $POST_TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val postId = cursor.getInt(cursor.getColumnIndexOrThrow(POST_ID))
                val content = cursor.getString(cursor.getColumnIndexOrThrow(POST_CONTENT))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(POST_DATE))
                // val tagList = getAllTagByPostId(postId)
                val imageId = cursor.getInt(cursor.getColumnIndexOrThrow(POST_IMG_ID))

                val post = PostData(postId, content, date,/*tagList, */imageId)
                postList.add(post)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return postList
    }


    // C 태그 테이블에 새 레코드 삽입
    private fun insertTag(postId: Int, userId: Int): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(POSTTAG_POST_ID, postId)
            put(POSTTAG_CONTACT_ID, userId)
        }
        val result = db.insert(POST_TAGS_TABLE_NAME, null, values)
        db.close()
        return result
    }

    // 조회
    fun getAllContactIdByPostId(postId: Int): Set<Int> { // post id를 받아서 포함된 모든 contact Id 반환
        val tagSet: MutableSet<Int> = mutableSetOf() // MutableSet 사용
        val db = this.readableDatabase
        val query = "SELECT * FROM $POST_TAGS_TABLE_NAME WHERE $POSTTAG_POST_ID = ?"
        val cursor = db.rawQuery(query, arrayOf("$postId"))
        if (cursor.moveToFirst()) {
            do {
                val contactId = cursor.getInt(cursor.getColumnIndexOrThrow(POSTTAG_CONTACT_ID))
                tagSet.add(contactId) // 값 추가
                Log.d("tegSet update check", "current item : $contactId")
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return tagSet
    }


    fun getContactByContactId(tagId: Int): ContactData? {
        val db = this.readableDatabase
        val selectQuery = """
            SELECT * FROM $CONTACT_TABLE_NAME WHERE $CONTACT_ID = ?
        """
        val cursor = db.rawQuery(selectQuery, arrayOf(tagId.toString()))

        cursor?.let {
            if (it.moveToFirst()) {
                val contact = ContactData(
                    it.getInt(it.getColumnIndexOrThrow(CONTACT_ID)),
                    it.getString(it.getColumnIndexOrThrow(CONTACT_NAME)),
                    it.getString(it.getColumnIndexOrThrow(CONTACT_PHONENUM)),
                    it.getInt(it.getColumnIndexOrThrow(CONTACT_IMG_ID))
                )
                cursor.close()
                return contact
            }
        }

        cursor?.close()
        return null
    }

    //D
    private fun deleteTagByPostId(postId: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(POST_TAGS_TABLE_NAME, "$POSTTAG_ID = ?", arrayOf(postId.toString()))
        db.close()
        return result
    }
}
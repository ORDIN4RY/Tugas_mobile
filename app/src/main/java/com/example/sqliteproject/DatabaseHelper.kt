package com.example.sqliteproject

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.sqliteproject.User

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "user_db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_USER = "user"

        private const val KEY_ID = "id"
        private const val KEY_USERNAME = "username"
        private const val KEY_TOKEN = "token"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_USER("
                + "$KEY_ID INTEGER PRIMARY KEY,"
                + "$KEY_USERNAME TEXT,"
                + "$KEY_TOKEN TEXT)")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        onCreate(db)
    }

    fun saveUser(user: User) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(KEY_ID, user.id)
            put(KEY_USERNAME, user.username)
            put(KEY_TOKEN, user.token)
        }
        db.insert(TABLE_USER, null, values)
        db.close()
    }

    fun getUser(): User? {
        val db = readableDatabase
        val cursor = db.query(TABLE_USER, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            val user = User(
                cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_USERNAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_TOKEN))
            )
            cursor.close()
            db.close()
            return user
        }
        cursor.close()
        db.close()
        return null
    }

    fun clearUser() {
        val db = writableDatabase
        db.delete(TABLE_USER, null, null)
        db.close()
    }
}

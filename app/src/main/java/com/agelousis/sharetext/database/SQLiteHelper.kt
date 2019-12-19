package com.agelousis.sharetext.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        // Table Name
        const val TABLE_NAME = "SAVED_TEXT"

        // Table columns
        const val ID = "id"
        const val CHANNEL = "channel"
        const val TEXT = "text"
        const val DATE = "date"

        // Database Information
        private const val DB_NAME = "SHARE_TEXT"

        // database version
        private const val DB_VERSION = 1

        // Creating table query
        private const val CREATE_TABLE = "CREATE TABLE $TABLE_NAME($ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$CHANNEL TEXT, $TEXT TEXT, $DATE TEXT);"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}
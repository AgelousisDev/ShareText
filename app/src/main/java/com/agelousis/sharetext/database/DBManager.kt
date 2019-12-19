package com.agelousis.sharetext.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.agelousis.sharetext.main.ui.saved.models.SavedMessageModel

class DBManager(context: Context) {

    private var sqLiteHelper: SQLiteHelper? = null
    private var database: SQLiteDatabase? = null

    init {
        sqLiteHelper = SQLiteHelper(context = context)
        database = sqLiteHelper?.writableDatabase
    }

    fun close() {
        sqLiteHelper?.close()
    }

    fun insert(channel: String, text: String, date: String) {
        val contentValue = ContentValues()
        contentValue.put(SQLiteHelper.CHANNEL, channel)
        contentValue.put(SQLiteHelper.TEXT, text)
        contentValue.put(SQLiteHelper.DATE, date)
        database?.insert(SQLiteHelper.TABLE_NAME, null, contentValue)
    }

    fun fetch(): ArrayList<SavedMessageModel>? {
        val listOfSavedMessageModel = arrayListOf<SavedMessageModel>()
        val cursor: Cursor? = database?.query(SQLiteHelper.TABLE_NAME, arrayOf(SQLiteHelper.ID, SQLiteHelper.CHANNEL, SQLiteHelper.TEXT, SQLiteHelper.DATE), null, null, null, null, null)
        if (cursor?.moveToFirst() == true)
            do {
                listOfSavedMessageModel.add(SavedMessageModel(channel = cursor.getString(cursor.getColumnIndex(SQLiteHelper.CHANNEL)),
                    text = cursor.getString(cursor.getColumnIndex(SQLiteHelper.TEXT)), date = cursor.getString(cursor.getColumnIndex(SQLiteHelper.DATE))))
            } while (cursor.moveToNext())
        cursor?.close()
        return listOfSavedMessageModel
    }

    fun delete(id: Long) {
        database?.delete(SQLiteHelper.TABLE_NAME,  "${SQLiteHelper.ID}=$id", null)
    }
}
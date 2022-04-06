package com.example.giuaki


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {

        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                NAME_COl + " TEXT," +
                EMAIL_COL + " TEXT" +
                CONTACT_COL + " TEXT" +
                ADDRESS_COL + " TEXT" + ")")


        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun addName(name : String, email : String, contact :String, address:String ){


        val values = ContentValues()

        values.put(NAME_COl, name)
        values.put(EMAIL_COL, email)
        values.put(CONTACT_COL, contact)
        values.put(ADDRESS_COL, address)


        val db = this.writableDatabase

        db.insert(TABLE_NAME, null, values)


        db.close()
    }


    fun getName(): Cursor? {


        val db = this.readableDatabase


        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null)

    }

    companion object{

        private val DATABASE_NAME = "GEEKS_FOR_GEEKS"

        private val DATABASE_VERSION = 1

        val TABLE_NAME = "gfg_table"

        val ID_COL = "id"

        val NAME_COl = "name"

        val EMAIL_COL = "email"

        val CONTACT_COL = "contact"

        val ADDRESS_COL = "address"
    }
}

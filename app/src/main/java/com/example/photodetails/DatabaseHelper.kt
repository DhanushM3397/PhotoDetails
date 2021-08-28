package com.example.photodetails

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import java.io.File


class DatabaseHelper(context: Context) : Application() {
    private var functionsCall: FunctionsCall = FunctionsCall()
    private var DATABASE_PATH: String? = null
    private var DATABASE_NAME: String? = null
    private var mh: MyHelper? = null
    private var myDataBase: SQLiteDatabase? = null

    init {
        DATABASE_PATH =
            java.lang.String.format("%s%s", functionsCall.filepath("Database"), File.separator)
         DATABASE_NAME = String.format("%s%s", DATABASE_PATH, "photodata.db")
        mh = MyHelper(context, DATABASE_NAME, null, 2)
    }

    fun insertdata(photoClass: PhotoClass): Long {
         open()
        val contentValues = ContentValues()
        contentValues.put("sp_order", photoClass.sp_order)
        contentValues.put("OrderFrom", photoClass.orderFrom)
        contentValues.put("OrderLocation", photoClass.orderLocation)
        contentValues.put("orderTotalAmt", photoClass.orderTotalAmt)
        contentValues.put("orderAdvanceAmt", photoClass.orderAdvanceAmt)
        contentValues.put("NoOfDaysEvent", photoClass.orderDate)

        val result: Long = myDataBase!!.insert("PhotoTable", null, contentValues)
        close()
        return result
    }

    fun getAllTransport(): ArrayList<PhotoClass> {
        val transportModelArrayList: ArrayList<PhotoClass> = ArrayList<PhotoClass>()
        // Select All Query
        val selectQuery = "SELECT  * FROM PhotoTable "
        open()

        val cursor = myDataBase!!.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                val photoClass = PhotoClass()
                photoClass.sp_order = cursor.getString(1)
                photoClass.orderFrom = cursor.getString(2)
                photoClass.orderLocation = cursor.getString(3)
                photoClass.orderTotalAmt = cursor.getString(4)
                photoClass.orderAdvanceAmt = cursor.getString(5)
                photoClass.orderDate = cursor.getString(6)



                // Adding contact to list
                transportModelArrayList.add(photoClass)
            } while (cursor.moveToNext())
        }
        close()
        // return contact list
        return transportModelArrayList
    }

    //Open database
     fun open() {
        myDataBase = mh!!.writableDatabase
    }





    //Close database
    fun close() {
        myDataBase!!.close()
    }

    private fun checkDataBase(): Boolean {
        var checkDB: SQLiteDatabase? = null
        try {
            val myPath: String = DATABASE_PATH + "photodata.db"
            val file = File(myPath)
            if (file.exists() && !file.isDirectory) checkDB =
                SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE)
        } catch (e: SQLiteException) {
            Toast.makeText(baseContext, "databse not getting", Toast.LENGTH_SHORT).show()
        }
        checkDB?.close()
        return if (checkDB != null) true else false
    }
}


private class MyHelper(context: Context?, name: String?, factory: CursorFactory?, version: Int) :
    SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE PhotoTable(_id INTEGER  PRIMARY KEY ,sp_order TEXT ,OrderFrom TEXT,OrderLocation TEXT,orderTotalAmt TEXT,orderAdvanceAmt Text,NoOfDaysEvent Text)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // if (newVersion > oldVersion) db.execSQL("ALTER TABLE PhotoTable ADD NoOfDaysEvent TEXT")
    }


}

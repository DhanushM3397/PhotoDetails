package com.example.photodetails

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class FunctionsCall {
    fun filepath(value: String): String? {
        val dir = File(
            Environment.getExternalStorageDirectory(), AppFolderName()
                    + File.separator + value
        )
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir.toString()
    }

    fun AppFolderName(): String {
        return "PhotoStudio" + File.separator + "OrderDetails" + File.separator + "files"
    }

    fun showToastMethod(context: Context?, msg: String) {
        Toast.makeText(context, " $msg", Toast.LENGTH_SHORT).show()
    }

    fun Parse_Date9(time: String?): String? {
        val input = "d-M-yyyy"
        val output = "dd-MM-yyyy"
        val inputFormat = SimpleDateFormat(input, Locale.getDefault())
        val outputFormat = SimpleDateFormat(output, Locale.getDefault())
        val date: Date
        var str: String? = null
        try {
            date = inputFormat.parse(time)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str
    }

    fun getCalculatedDate(date: String?, dateFormat: String?, days: Int): String? {
        var str:String=""
        val cal = Calendar.getInstance()
        val s = SimpleDateFormat(dateFormat)
        cal.add(Calendar.DAY_OF_YEAR, days)
        try {
            str=s.format(Date(s.parse(date).time)).toString()
            return str
        } catch (e: ParseException) {
            // TODO Auto-generated catch block
            Log.e("TAG", "Error in Parsing Date : " + e.message)
        }
        return str
    }


}
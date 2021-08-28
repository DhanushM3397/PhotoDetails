package com.example.photodetails

//import com.example.photodetails.MainActivity.Companion.reportlist

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils
import android.widget.RemoteViews
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.photodetails.MainActivity.Companion.dataBaseArtists
import com.example.photodetails.MainActivity.Companion.reportlist
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PhotoDetailDisplay : AppCompatActivity() {

    private val NOTIFICATION_TITLE = "EVents Notification "
    private val CONTENT_TEXT = "Click to Information of Event Orders"
    lateinit var functionsCall: FunctionsCall

    lateinit var databaseHelper: DatabaseHelper
    lateinit var reportDatadisplayAdapter: ReportDatadisplayAdapter
    lateinit var recyclerView: RecyclerView

    var date: String = ""
    lateinit var calendar: Calendar
    lateinit var listData: MutableList<PhotoClass>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_detail_display)
        setSupportActionBar(findViewById(R.id.toolbar))

        databaseHelper = DatabaseHelper(baseContext)
        functionsCall = FunctionsCall()

        //----------list to store data from database
        reportlist = databaseHelper.getAllTransport()

        //list to store data from Firebase
        listData = ArrayList()
        recyclerView = findViewById(R.id.recy_Report)

        // recyclerview()

        calendar = Calendar.getInstance()
        val simpledateformat = SimpleDateFormat("dd-MM-yyyy")
        date = simpledateformat.format(calendar.time).toString()


        val date1: String = functionsCall.getCalculatedDate(date, "dd-MM-yyyy", -2).toString()


    }

    /* --------------This is For Retrieving the  data from Firebase and Set to the Recyclerview------------------------------- */

    override fun onStart() {
        super.onStart()
        dataBaseArtists.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (npsnapshot in snapshot.children) {
                        val l: PhotoClass = npsnapshot.getValue(PhotoClass::class.java)!!
                        listData.add(l)
                        recyclerview(listData)

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    @SuppressLint("WrongConstant")
    private fun recyclerview(list: MutableList<PhotoClass>) {

        recyclerView.layoutManager = object : LinearLayoutManager(this) {
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                // force height of viewHolder here, this will override layout_height from xml
                lp.height = height / (3)
                return true
            }
        }
        reportDatadisplayAdapter = ReportDatadisplayAdapter(list)
        list.reverse()

        recyclerView.setHasFixedSize(false)
        recyclerView.isNestedScrollingEnabled = false

        /*val columns = 2
        val layoutManager = GridLayoutManager(applicationContext, columns)
        recyclerView.layoutManager =layoutManager*/
        // recyclerView.layoutManager = LinearLayoutManager(applicationContext, 1, true)
        recyclerView.adapter = reportDatadisplayAdapter

        for (i in 0 until list.size) {
            if (list[0].orderDate.length < 10) {
                if (list[0].orderDate == date) {
                    sendNotification()
                    Toast.makeText(this, date, Toast.LENGTH_SHORT).show()
                }
            } else {
                val v = list[0].orderDate.substring(0, 10)
                if (v == date) {
                    sendNotification()
                    Toast.makeText(this, v, Toast.LENGTH_SHORT).show()
                }
            }


        }


    }

    private fun sendNotification() {
        val expandedView = RemoteViews(packageName, R.layout.view_expanded_notification)
        expandedView.setTextViewText(
            R.id.timestamp,
            DateUtils.formatDateTime(this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME)
        )
        expandedView.setTextViewText(R.id.notification_message, listData[0].orderDate)

        // adding action to left button
        val leftIntent = Intent(this, NotificationIntentService::class.java)
        leftIntent.action = "left"
        expandedView.setOnClickPendingIntent(
            R.id.left_button,
            PendingIntent.getService(this, 1, leftIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        )
        // adding action to right button
        val rightIntent = Intent(this, NotificationIntentService::class.java)
        rightIntent.action = "right"
        expandedView.setOnClickPendingIntent(
            R.id.right_button,
            PendingIntent.getService(this, 0, rightIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        )
        val collapsedView = RemoteViews(packageName, R.layout.view_collapsed_notification)
        collapsedView.setTextViewText(
            R.id.timestamp,
            DateUtils.formatDateTime(this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME)
        )
        val builder =
            NotificationCompat.Builder(this) // these are the three things a NotificationCompat.Builder object requires at a minimum
                .setSmallIcon(R.drawable.ic_pawprint)
                .setContentTitle(NOTIFICATION_TITLE)
                .setContentText(CONTENT_TEXT) // notification will be dismissed when tapped
                .setAutoCancel(true) // tapping notification will open MainActivity
                .setContentIntent(
                    PendingIntent.getActivity(
                        this,
                        0,
                        Intent(this, MainActivity::class.java),
                        0
                    )
                ) // setting the custom collapsed and expanded views
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView) // setting style to DecoratedCustomViewStyle() is necessary for custom views to display
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())




        // retrieves android.app.NotificationManager
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, builder.build())
    }


}

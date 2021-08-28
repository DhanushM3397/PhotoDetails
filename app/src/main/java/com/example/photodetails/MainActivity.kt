package com.example.photodetails

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    companion object {
        lateinit var reportlist: ArrayList<PhotoClass>
        lateinit var dataBaseArtists: DatabaseReference
    }


    lateinit var photoClass: PhotoClass
    lateinit var databaseHelper: DatabaseHelper
    lateinit var functionCall: FunctionsCall

    var b = false
    private lateinit var linearLayout: LinearLayout
    private lateinit var orderSpinner: Spinner
    private lateinit var orderEventsDays: Spinner
    private lateinit var etOrderFrom: EditText
    private lateinit var etOrderLoc: EditText

    private lateinit var etOrderTotalAmt: EditText
    private lateinit var etOrderAdvanceAmt: EditText

    private lateinit var txOrderDate: TextView
    private lateinit var btSubmit: Button
    private lateinit var btReport: Button

    private lateinit var calendar: Calendar
    lateinit var constraintsBuilder: CalendarConstraints.Builder
    lateinit var days: String
    lateinit var spName: String
    lateinit var rangedate: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewId()


    }


    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    private fun findViewId() {

        // this firebase  Url for Storing Data
        dataBaseArtists =
            FirebaseDatabase.getInstance("https://fir-app-cd5b9-default-rtdb.firebaseio.com/")
                .getReference("Photo")


        functionCall = FunctionsCall()
        databaseHelper = DatabaseHelper(baseContext)

        linearLayout = findViewById(R.id.line_orderFrom)
        orderSpinner = findViewById(R.id.order_spinner)
        orderEventsDays = findViewById(R.id.order_eventsDays)
        orderEventsDays.onItemSelectedListener = this
        orderSpinner.onItemSelectedListener = this

        etOrderFrom = findViewById(R.id.order_From)


        etOrderLoc = findViewById(R.id.order_Location)
        etOrderTotalAmt = findViewById(R.id.order_TotalAmt)
        etOrderAdvanceAmt = findViewById(R.id.order_AdvanceAmt)
        txOrderDate = findViewById(R.id.order_EventDate)


        txOrderDate.setOnClickListener {
            if (days == "One Day Event") {
                singleDaySelection()
            } else {
                rangeOfDaysSelection()
            }
        }



        btSubmit = findViewById(R.id.bt_submit)
        btSubmit.setOnClickListener {
            validate()
        }
        btReport = findViewById(R.id.bt_report)
        btReport.setOnClickListener {
            val intent = Intent(this, PhotoDetailDisplay::class.java)
            startActivity(intent)
        }


    }


    @SuppressLint("SetTextI18n")
    override fun onItemSelected(adapterView: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (adapterView!!.id) {
            R.id.order_spinner -> {
                spName = "" + orderSpinner.selectedItem
                if (spName == "Own Order") {
                    linearLayout.visibility = View.GONE
                    b = true
                } else {
                    linearLayout.visibility = View.VISIBLE
                    b = false
                }
                Toast.makeText(this, "" + spName, Toast.LENGTH_SHORT).show()
            }
            R.id.order_eventsDays -> {
                days = "" + orderEventsDays.selectedItem
                txOrderDate.text = "Event day"
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


    private fun validate() {

        //getting a unique id using push().getKey() method
        //it will create a unique id and we will use it as the Primary Key for our Artist
        //getting a unique id using push().getKey() method
        //it will create a unique id and we will use it as the Primary Key for our Artist
       // FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser()


        val id: String = dataBaseArtists.push().key.toString()

        when {
            orderSpinner.equals("---select Order---") -> {
                Toast.makeText(this, "please select any order", Toast.LENGTH_SHORT).show()
            }
           /* linearLayout.isShown -> {
                if (TextUtils.isEmpty(etOrderFrom.text.toString())) {
                    etOrderFrom.error = "please enter the order From"
                    etOrderFrom.requestFocus()
                    return
                }

            }*/
            TextUtils.isEmpty(etOrderFrom.text.toString())-> {
                etOrderFrom.error = "please enter the order From"
                etOrderFrom.requestFocus()
                return
            }
            TextUtils.isEmpty(etOrderLoc.text.toString()) -> {
                etOrderLoc.error = "please enter the order Location"
                etOrderLoc.requestFocus()
                return
            }
            TextUtils.isEmpty(etOrderTotalAmt.text.toString()) -> {
                etOrderTotalAmt.error = "please enter the Total Event Amount"
                etOrderTotalAmt.requestFocus()
                return
            }
            TextUtils.isEmpty(etOrderAdvanceAmt.text.toString()) -> {
                etOrderAdvanceAmt.error = "please enter the order Location"
                etOrderAdvanceAmt.requestFocus()
                return
            }


        }
        photoClass = PhotoClass()
        photoClass.sp_order = spName
        photoClass.orderFrom = etOrderFrom.text.toString()
        photoClass.orderLocation = etOrderLoc.text.toString()
        photoClass.orderTotalAmt = etOrderTotalAmt.text.toString()
        photoClass.orderAdvanceAmt = etOrderAdvanceAmt.text.toString()
        photoClass.orderDate = txOrderDate.text.toString()
        photoClass.orderRemainAmt =
            (photoClass.orderTotalAmt.toInt() - photoClass.orderAdvanceAmt.toInt()).toString()





        //insert the dataclass data into firebase
        dataBaseArtists.child(id).setValue(photoClass).addOnSuccessListener {
            Toast.makeText(applicationContext, "record save", Toast.LENGTH_LONG).show()
            etOrderLoc.text.clear()
            etOrderTotalAmt.text.clear()
            etOrderAdvanceAmt.text.clear()
            txOrderDate.text = "Event day"
        }.addOnFailureListener {
            Toast.makeText(applicationContext, "record NotSaved", Toast.LENGTH_LONG).show()
        }


        /* val insertResopnse = databaseHelper.insertdata(photoClass)
         if (insertResopnse > -1) {



         } else {
             Toast.makeText(
                 applicationContext,
                 "id or name or email cannot be blank",
                 Toast.LENGTH_LONG
             ).show()
         }*/
    }

    private fun singleDaySelection() {
        calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(
            this@MainActivity,
            { datePicker, year, month, day ->

                rangedate = dayOfMonth.toString() + "-" + (month + 1) + "-" + year.toString()

                val date = functionCall.Parse_Date9(rangedate)
                txOrderDate.text = date
            }, year, month, dayOfMonth

        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()

        Toast.makeText(baseContext, "On the Processing ", Toast.LENGTH_SHORT).show()
    }


    private fun rangeOfDaysSelection() {

        calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.clear()
        /* val today: Long = MaterialDatePicker.todayInUtcMilliseconds()
        // calendar.timeInMillis == today*/
        constraintsBuilder = CalendarConstraints.Builder()
        val builder1 = MaterialDatePicker.Builder.dateRangePicker()
        builder1.setTitleText("Select the Date")


        val meterialDatePicker: MaterialDatePicker<Pair<Long, Long>> = builder1.build()


        meterialDatePicker.show(this.supportFragmentManager, "DATE_PICKER")
        meterialDatePicker.addOnPositiveButtonClickListener(
            MaterialPickerOnPositiveButtonClickListener {

                val selectedDates = meterialDatePicker.selection as Pair<*, *>
//              then obtain the startDate & endDate from the range
                //              then obtain the startDate & endDate from the range
                val rangeDate1 = Pair(
                    Date((selectedDates.first as Long?)!!),
                    Date((selectedDates.second as Long?)!!)
                )
//              assigned variables
                //              assigned variables
                val startDate = rangeDate1.first
                val endDate = rangeDate1.second
//              Format the dates in ur desired display mode
                //              Format the dates in ur desired display mode
                val simpleFormat = SimpleDateFormat("dd-MM-yyyy")
//              Display it by setText
                //              Display it by setText

                rangedate =
                    simpleFormat.format(startDate) + "\n" + simpleFormat.format(endDate)
                txOrderDate.text = rangedate
                Toast.makeText(this, txOrderDate.text.toString(), Toast.LENGTH_SHORT).show()
            })
    }


}
package com.example.photodetails


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ReportDatadisplayAdapter(var photoclas: MutableList<PhotoClass>?) :
    RecyclerView.Adapter<ReportDatadisplayAdapter.Myhelper>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Myhelper {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.datareportadapter, parent, false)
        val layoutParams: ViewGroup.LayoutParams = view.getLayoutParams()
        layoutParams.height = (parent.height * 0.3).toInt()
        view.setLayoutParams(layoutParams)

        return Myhelper(view)
    }

    override fun onBindViewHolder(holder: Myhelper, position: Int) {
        val bbcModel: PhotoClass = photoclas!![position]

        if (bbcModel != null) {
            holder.adapt_eventOrder.text = bbcModel.sp_order
            holder.adapt_OrderFrom.text = bbcModel.orderFrom
            holder.adapt_eventLocation.text = bbcModel.orderLocation
            holder.adapt_eventTotalAmt.text = bbcModel.orderTotalAmt
            holder.adapt_eventAdvanceAmt.text = bbcModel.orderAdvanceAmt
            holder.adapt_eventDate.text = bbcModel.orderDate
            holder.adapt_eventRemainAmt.text =
                (bbcModel.orderTotalAmt.toInt() - bbcModel.orderAdvanceAmt.toInt()).toString()

        }


    }

    override fun getItemCount(): Int = photoclas!!.size

    class Myhelper(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var adapt_eventOrder: TextView = itemView.findViewById(R.id.adapt_eventOrder)
        var adapt_OrderFrom: TextView = itemView.findViewById(R.id.adapt_OrderFrom)
        var adapt_eventLocation: TextView = itemView.findViewById(R.id.adapt_eventLocation)
        var adapt_eventTotalAmt: TextView = itemView.findViewById(R.id.adapt_eventTotalAmt)
        var adapt_eventAdvanceAmt: TextView = itemView.findViewById(R.id.adapt_eventAdvanceAmt)
        var adapt_eventDate: TextView = itemView.findViewById(R.id.adapt_orderDate)
        var adapt_eventRemainAmt: TextView = itemView.findViewById(R.id.adapt_eventRemainAmt)


        val linearLayout: LinearLayout = itemView.findViewById(R.id.line2)

    }

}
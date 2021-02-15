package com.afrolone.iusapp.ui.calendar

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.afrolone.iusapp.R

class CalendarAdapter(private var calendarRow: ArrayList<String>)
    : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    val TAG = "Caladapter"

    fun setData(data: ArrayList<String>) {
        calendarRow = data
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.calendar_item, viewGroup, false)

        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        Log.v(TAG, "pair in adapter: ${calendarRow[position]}")

        //TODO Change with pair data type

        viewHolder.eventTV.text = calendarRow[position].split("#")[0]
        viewHolder.dateTV.text = calendarRow[position].split("#")[1]
    }

    override fun getItemCount() = calendarRow.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val eventTV: TextView = view.findViewById(R.id.eventTV)
        val dateTV: TextView = view.findViewById(R.id.dateTV)
    }
}
package com.afrolone.iusapp.ui.schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.afrolone.iusapp.R
import com.afrolone.iusapp.models.Grade

class ScheduleRowAdapter(
        private var scheduleRow: ArrayList<ArrayList<String>>,
        private val listener: OnItemClickListener
) : RecyclerView.Adapter<ScheduleRowAdapter.ViewHolder>() {

    fun setData(data: ArrayList<ArrayList<String>>) {
        scheduleRow = data
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.course_schedule_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.hoursTV1.text = scheduleRow[position][0].split("-")[0].trim()
        viewHolder.hoursTV3.text = scheduleRow[position][0].split("-")[1].trim()
        viewHolder.mondayTV1.text = splitIntoNumbersCars(scheduleRow[position][1])[0]
        viewHolder.mondayTV2.text = splitIntoNumbersCars(scheduleRow[position][1])[1]
        viewHolder.tuesdayTV1.text = splitIntoNumbersCars(scheduleRow[position][2])[0]
        viewHolder.tuesdayTV2.text = splitIntoNumbersCars(scheduleRow[position][2])[1]
        viewHolder.wednesdayTV1.text = splitIntoNumbersCars(scheduleRow[position][3])[0]
        viewHolder.wednesdayTV2.text = splitIntoNumbersCars(scheduleRow[position][3])[1]
        viewHolder.thursdayTV1.text = splitIntoNumbersCars(scheduleRow[position][4])[0]
        viewHolder.thursdayTV2.text = splitIntoNumbersCars(scheduleRow[position][4])[1]
        viewHolder.fridayTV1.text = splitIntoNumbersCars(scheduleRow[position][5])[0]
        viewHolder.fridayTV2.text = splitIntoNumbersCars(scheduleRow[position][5])[1]
    }

    fun splitIntoNumbersCars(str: String): List<String>{
        var num = ""
        var alpha = ""
        if (str.isBlank()){
            return listOf(alpha, num)
        } else {
            for(i in str.indices) {
                if(str[i].isDigit()){
                    num += str[i]
                } else {
                    alpha += str[i]
                }
            }
            return listOf(alpha, num)
        }

    }

    override fun getItemCount() = scheduleRow.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val hoursTV1: TextView = view.findViewById(R.id.hoursTV1)
        val hoursTV3: TextView = view.findViewById(R.id.hoursTV3)
        val mondayTV1: TextView = view.findViewById(R.id.mondayTV1)
        val mondayTV2: TextView = view.findViewById(R.id.mondayTV2)
        val tuesdayTV1: TextView = view.findViewById(R.id.tuesdayTV1)
        val tuesdayTV2: TextView = view.findViewById(R.id.tuesdayTV2)
        val wednesdayTV1: TextView = view.findViewById(R.id.wednesdayTV1)
        val wednesdayTV2: TextView = view.findViewById(R.id.wednesdayTV2)
        val thursdayTV1: TextView = view.findViewById(R.id.thursdayTV1)
        val thursdayTV2: TextView = view.findViewById(R.id.thursdayTV2)
        val fridayTV1: TextView = view.findViewById(R.id.fridayTV1)
        val fridayTV2: TextView = view.findViewById(R.id.fridayTV2)

        init {
            view.setOnClickListener(this)
        }


        override fun onClick(v: View?) {
            listener.onItemClick(position)
        }

    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

}
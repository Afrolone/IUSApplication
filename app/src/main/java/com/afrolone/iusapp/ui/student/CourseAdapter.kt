package com.afrolone.iusapp.ui.student

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.afrolone.iusapp.R
import com.afrolone.iusapp.models.Course

class CourseAdapter(
    private var courses: ArrayList<Course>,
    private val listener: OnItemClickListener
) :
        RecyclerView.Adapter<CourseAdapter.ViewHolder>() {

    fun setData(data: ArrayList<Course>) {
        courses = data
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.course_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.specialtv.text = courses[position].courseName
        viewHolder.letterGradetv.text = courses[position].letterGrade.subSequence(0,2).toString() //.subSequence(0,1).trim()
    }

    /*
    fun getLetterGrade(gradeRaw: String): String {
        return if(gradeRaw.trim().length == 2) {
            gradeRaw
        } else {
            (gradeRaw.trim() + " ")
        }
    } */

    override fun getItemCount() = courses.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val specialtv: TextView = view.findViewById(R.id.specialtv)
        val letterGradetv: TextView = view.findViewById(R.id.letterGradetv)

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

package com.afrolone.iusapp.ui.course

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.afrolone.iusapp.R
import com.afrolone.iusapp.models.Course
import com.afrolone.iusapp.models.Grade

class GradeAdapter(
        private var grades: ArrayList<Grade>,
        private val listener: OnItemClickListener
) : RecyclerView.Adapter<GradeAdapter.ViewHolder>() {

    fun setData(data: ArrayList<Grade>) {
        grades = data
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.grade_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.gradeNameTV.text = (
                grades[position].gradeName + " " +
                grades[position].percentage + "%")
        viewHolder.gradeNumericalTV.text = grades[position].grade
    }

    override fun getItemCount() = grades.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val gradeNameTV: TextView = view.findViewById(R.id.gradeNameTV)
        val gradeNumericalTV: TextView = view.findViewById(R.id.gradeNumericalTV)

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
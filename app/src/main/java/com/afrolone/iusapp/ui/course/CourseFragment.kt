package com.afrolone.iusapp.ui.course

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.afrolone.iusapp.R
import com.afrolone.iusapp.models.Course
import com.afrolone.iusapp.scrapeutils.ScrapeUtils
import kotlinx.android.synthetic.main.fragment_course.*

class CourseFragment : Fragment(), GradeAdapter.OnItemClickListener {

    lateinit var COURSE: Course
    val gradeAdapter = GradeAdapter(ArrayList(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        COURSE = ScrapeUtils.COURSES[arguments?.getInt("position")!!]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()

        courseCodeTV.text = COURSE.courseCode
        courseNameTV.text = COURSE.courseName
        courseStaffTV.text = COURSE.staff
        yidTV.text = COURSE.yid
        courseFinalGradeTV.text = COURSE.courseFinalGrade
        letterGradeTV.text = COURSE.letterGrade
        semesterNameTV.text = COURSE.semesterName
    }

    fun setRecyclerView(){
        gradesRecyclerView.layoutManager = LinearLayoutManager(this.context)
        gradesRecyclerView.adapter = gradeAdapter
        gradeAdapter.setData(COURSE.grades)
    }

    override fun onItemClick(position: Int) {

    }
}
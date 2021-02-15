package com.afrolone.iusapp.ui.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.afrolone.iusapp.R
import com.afrolone.iusapp.db.userdata.UserPrefs
import com.afrolone.iusapp.models.Course
import com.afrolone.iusapp.scrapeutils.ScrapeUtils
import com.afrolone.iusapp.ui.course.GradeAdapter
import kotlinx.android.synthetic.main.fragment_course.*
import kotlinx.android.synthetic.main.fragment_schedule.*


class ScheduleFragment : Fragment(), ScheduleRowAdapter.OnItemClickListener {

    var USER_ALREADY_SAVED: Boolean = false
    var loaded: Boolean = false

    lateinit var schedule: ArrayList<ArrayList<String>>
    val scheduleRowAdapter: ScheduleRowAdapter = ScheduleRowAdapter(ArrayList(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        schedule = ScrapeUtils.scheduleRows
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()

        //checkUserPrefs()

        /*
        if(ScrapeUtils.IS_LOGGED_IN) {
            scheduleContainer.visibility = View.VISIBLE
            logInDialogContainer.visibility = View.INVISIBLE
        } else {
            scheduleContainer.visibility = View.INVISIBLE
            logInDialogContainer.visibility = View.VISIBLE
        } */
    }

    fun setRecyclerView(){
        scheduleRecyclerView.layoutManager = LinearLayoutManager(this.context)
        scheduleRecyclerView.adapter = scheduleRowAdapter
        scheduleRowAdapter.setData(schedule)
    }

    fun checkUserPrefs() {
        USER_ALREADY_SAVED = UserPrefs.checkPrefs()
        if (USER_ALREADY_SAVED){
            ScrapeUtils.USERNAME = UserPrefs.get(UserPrefs.USERNAME_CODE)!!
            ScrapeUtils.PASSWORD = UserPrefs.get(UserPrefs.PASSWORD_CODE)!!
            ScrapeUtils.IS_LOGGED_IN = true
        }
    }

    override fun onItemClick(position: Int) {

    }
}
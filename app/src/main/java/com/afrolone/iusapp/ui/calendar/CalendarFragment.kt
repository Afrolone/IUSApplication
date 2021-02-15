package com.afrolone.iusapp.ui.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.afrolone.iusapp.R
import com.afrolone.iusapp.scrapeutils.ScrapeUtils
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CalendarFragment : Fragment() {


    lateinit var calendarRows: ArrayList<String>
    val calendarAdapter: CalendarAdapter = CalendarAdapter(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        GlobalScope.launch {
            ScrapeUtils.getAcademicCalendar()
            calendarRows = ScrapeUtils.getCalendarRows()
            activity?.runOnUiThread {
                calendarAdapter.setData(calendarRows)
                calendarAdapter.notifyDataSetChanged()
            }
        }
    }

    fun setRecyclerView(){
        calendarRecyclerView.layoutManager = LinearLayoutManager(this.context)
        calendarRecyclerView.adapter = calendarAdapter
    }
}
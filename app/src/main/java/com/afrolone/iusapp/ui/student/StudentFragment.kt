package com.afrolone.iusapp.ui.student

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afrolone.iusapp.R
import com.afrolone.iusapp.db.userdata.UserPrefs
import com.afrolone.iusapp.scrapeutils.ScrapeUtils
import com.afrolone.iusapp.ui.course.CourseFragment
import com.afrolone.iusapp.ui.student.CourseAdapter
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_student.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.log

class StudentFragment : Fragment(), CourseAdapter.OnItemClickListener {

    var USER_ALREADY_SAVED: Boolean = false
    var loaded: Boolean = false
    val courseAdapter: CourseAdapter =
        CourseAdapter(arrayListOf(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkUserPrefs()

        if(ScrapeUtils.IS_LOGGED_IN){
            //requireView().findNavController().navigate(R.id.loginFragment)
            setRecyclerView()
            setUI()
            setAndFetchData() // coroutine used
        } else {
            setRecyclerView()
            setUI()
        }



    }

    private fun setAndFetchData() {
        if (ScrapeUtils.IS_FETCHED) {
            populateTextViews()
            loaded = true
            setAdapter()
            setUI()
        } else {
            GlobalScope.launch(Dispatchers.Default) {
                ScrapeUtils.getData()
                activity?.runOnUiThread {
                    populateTextViews()
                    loaded = true
                    setAdapter()
                    setUI()
                }
            }
        }
    }

    private fun setRecyclerView() {
        coursesRecyclerView.layoutManager = LinearLayoutManager(this.context)
        coursesRecyclerView.adapter = courseAdapter
    }

    private fun setAdapter() {
        courseAdapter.setData(ScrapeUtils.COURSES)
        courseAdapter.notifyDataSetChanged()
    }

    private fun populateTextViews() {
        studentNameTV.text = (ScrapeUtils.STUDENT.name + " " + ScrapeUtils.STUDENT.surname)
        studentIDTV.text = ScrapeUtils.STUDENT.ID
        studentProgramTV.text = ScrapeUtils.STUDENT.program
        studentFacultyTV.text = ScrapeUtils.STUDENT.faculty
        studentGPATV.text = ScrapeUtils.STUDENT.gpa
    }

    fun setUI() {
        logInButton.setOnClickListener {
            requireView().findNavController().navigate(R.id.loginFragment)
        }

        if (ScrapeUtils.IS_LOGGED_IN){
            if (loaded){
                mainprogressBar.visibility = View.INVISIBLE
                studentInfoContainer.visibility = View.VISIBLE
                coursesRecyclerView.visibility = View.VISIBLE
                logInDialogContainer.visibility = View.INVISIBLE
            } else {
                mainprogressBar.visibility = View.VISIBLE
                studentInfoContainer.visibility = View.INVISIBLE
                coursesRecyclerView.visibility = View.INVISIBLE
                logInDialogContainer.visibility = View.INVISIBLE
            }
        } else {
            mainprogressBar.visibility = View.INVISIBLE
            studentInfoContainer.visibility = View.INVISIBLE
            coursesRecyclerView.visibility = View.INVISIBLE
            logInDialogContainer.visibility = View.VISIBLE
        }

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
        val bundle = bundleOf("position" to position)
        requireView().findNavController().navigate(R.id.courseFragment, bundle)
    }
}

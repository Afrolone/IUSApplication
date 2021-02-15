package com.afrolone.iusapp.ui.login

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.afrolone.iusapp.R
import com.afrolone.iusapp.db.userdata.UserPrefs
import com.afrolone.iusapp.scrapeutils.ScrapeUtils
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var credsNotOK: Boolean = false
    private val loginContext = this.context
    private val loginFragmentReference = this

    /*
    Should add view password option in the login
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setActionBar()

        errorTextView.visibility = View.INVISIBLE
        loadingTextView.visibility = View.INVISIBLE

        loginButton.setOnClickListener {
            hideSoftKeyboard(this.requireActivity())
            setUserData()
            loadingTextView.visibility = View.VISIBLE
            GlobalScope.launch {
                credsNotOK = checkCredentials()

                setUserPrefs()

                activity?.runOnUiThread {
                    if (credsNotOK) {
                        errorTextView.visibility = View.VISIBLE
                        loadingTextView.visibility = View.INVISIBLE
                    } else {
                        Navigation.findNavController(this@LoginFragment.requireView()).navigate(R.id.studentFragment)
                    }
                }
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun setUserData() {
        ScrapeUtils.setUser(
                user = usernameEdittext.text.toString(),
                pass = passwordEdittext.text.toString()
        )
    }

    private fun checkCredentials(): Boolean {
        return ScrapeUtils.hasError()
    }


    private fun setActionBar() {
        //my_toolbar.title = "IUS Application"
    }

    fun hideSoftKeyboard(activity: Activity) {
        activity.currentFocus?.let {
            val inputMethodManager = ContextCompat.getSystemService(activity, InputMethodManager::class.java)!!
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    fun setUserPrefs() {
        if (usernameCheckBox.isChecked && !credsNotOK){
            UserPrefs.put(UserPrefs.REMEMBER_CODE, UserPrefs.REMEMBER_ME)
            UserPrefs.put(UserPrefs.USERNAME_CODE, ScrapeUtils.USERNAME)
            UserPrefs.put(UserPrefs.PASSWORD_CODE, ScrapeUtils.PASSWORD)
        }
    }
}
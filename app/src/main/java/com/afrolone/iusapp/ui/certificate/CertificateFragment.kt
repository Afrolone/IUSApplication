package com.afrolone.iusapp.ui.certificate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afrolone.iusapp.R
import com.afrolone.iusapp.scrapeutils.ScrapeUtils
import kotlinx.android.synthetic.main.fragment_certificate.*
import kotlinx.android.synthetic.main.fragment_certificate.logInDialogContainer

class CertificateFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_certificate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*

        if(ScrapeUtils.IS_LOGGED_IN) {
            studentCertificateApplicationContainer.visibility = View.VISIBLE
            logInDialogContainer.visibility = View.INVISIBLE
        } else {
            studentCertificateApplicationContainer.visibility = View.INVISIBLE
            logInDialogContainer.visibility = View.VISIBLE
        } */
    }
}
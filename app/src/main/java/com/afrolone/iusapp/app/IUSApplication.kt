package com.afrolone.iusapp.app

import android.app.Application
import android.content.Context

class IUSApplication: Application() {

    companion object {
        private lateinit var instance: IUSApplication
        fun getAppContext(): Context = instance.applicationContext
    }

    override fun onCreate() {
        instance = this
        super.onCreate()
    }

}
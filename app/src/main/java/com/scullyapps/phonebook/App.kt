package com.scullyapps.phonebook


import android.app.Application
import android.content.Context
import android.util.Log

class App : Application() {
    private val TAG: String = "App"

    init {
        instance = this
    }

    companion object {
        private var instance : App? = null

        fun getContext() : Context {
            return instance!!.applicationContext
        }
    }
}
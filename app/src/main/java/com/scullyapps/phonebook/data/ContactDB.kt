package com.scullyapps.phonebook.data


import android.app.Application
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.scullyapps.phonebook.App
import com.scullyapps.phonebook.models.Contact
import com.scullyapps.phonebook.models.ContactDAO

@Database(entities = arrayOf(Contact::class), version = 1)
abstract class ContactDB : RoomDatabase() {
    abstract fun contactDAO(): ContactDAO

    companion object {

        private var db : ContactDB? = null
        private var testdb : ContactDB? = null

        fun getInstance(): ContactDB {
            if(db == null) {
                db = Room.databaseBuilder(App.getContext(), ContactDB::class.java, "contact").build()
            }

            return db as ContactDB
        }

        fun getTestInstance(): ContactDB {
            if(testdb == null) {
                testdb = Room.databaseBuilder(App.getContext(), ContactDB::class.java, "test_contact").build()
            }

            return testdb as ContactDB
        }


        // Wrapper functions

        fun insert(c : Contact) {
            val db = getInstance()
            Thread {
                db.contactDAO().insert(c)
            }.start()
        }

        fun update(c : Contact) {
            val db = getInstance()
            Thread {
                db.contactDAO().update(c)
            }.start()
        }

        fun delete(c : Contact) {
            val db = getInstance()
            Thread {
                db.contactDAO().delete(c)
            }.start()
        }
    }
}
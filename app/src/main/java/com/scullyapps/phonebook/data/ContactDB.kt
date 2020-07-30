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

        fun getInstance(): ContactDB {
            if(db == null) {
                db = Room.databaseBuilder(App.getContext(), ContactDB::class.java, "contact").build()
            }

            return db as ContactDB
        }
    }
}
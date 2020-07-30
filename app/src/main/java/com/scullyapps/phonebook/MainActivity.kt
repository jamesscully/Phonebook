package com.scullyapps.phonebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.scullyapps.phonebook.data.ContactDB
import com.scullyapps.phonebook.models.Contact

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val db = Room.databaseBuilder(applicationContext, ContactDB::class.java, "contact").build()

        // we'll only need one instance of the database
        val db = ContactDB.getInstance()

        db.contactDAO().insert(Contact(1, "Test", "Name", "james.will.scully@gmail.com", "07827275818", "25 Dadadada"))

        db.contactDAO().getAll()


    }
}
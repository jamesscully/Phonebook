package com.scullyapps.phonebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.scullyapps.phonebook.adapters.ContactsRecyclerAdapter
import com.scullyapps.phonebook.data.ContactDB
import com.scullyapps.phonebook.data.ContactRepository
import com.scullyapps.phonebook.models.Contact
import com.scullyapps.phonebook.viewmodels.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private val model : MainActivityViewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recycler = contacts_recyclerview
        val adapter = ContactsRecyclerAdapter()
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)

        model.contacts.observe(this, Observer {contacts ->
            if(contacts.isNotEmpty()) {
                main_placeholder.visibility = View.INVISIBLE
                adapter.setData(contacts)
                adapter.notifyDataSetChanged()
            }
        })

    }
}
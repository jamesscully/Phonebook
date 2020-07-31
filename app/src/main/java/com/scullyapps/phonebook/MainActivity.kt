package com.scullyapps.phonebook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        // perform actions on click
        val adapter = ContactsRecyclerAdapter { contact ->
            launchContactDetails(EditDetailsActivity.State.VIEWING, contact)
        }

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)

        // If we have no contacts, show placeholder; else update recycler
        model.contacts.observe(this, Observer {contacts ->
            if(contacts.isNotEmpty()) {
                main_placeholder.visibility = View.INVISIBLE
                adapter.setData(contacts)
                adapter.notifyDataSetChanged()
            } else {
                main_placeholder.visibility = View.VISIBLE
            }
        })
    }

    //
    // Toolbar / Menu functions
    //

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.main_menu_newcontact -> {
                launchContactDetails(EditDetailsActivity.State.CREATING)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    // encapsulates the process of launching contact details, meaning a state will be required
    fun launchContactDetails(state : EditDetailsActivity.State, contact: Contact? = null) {
        val intent = Intent(this, EditDetailsActivity::class.java)
        intent.putExtra("state", state)

        if(contact != null) {
            intent.putExtra("contact", contact)
        }

        startActivity(intent)
    }

}
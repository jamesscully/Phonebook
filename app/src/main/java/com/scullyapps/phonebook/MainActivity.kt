package com.scullyapps.phonebook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding.widget.RxSearchView
import com.scullyapps.phonebook.adapters.ContactsRecyclerAdapter
import com.scullyapps.phonebook.data.ContactDB
import com.scullyapps.phonebook.models.Contact
import com.scullyapps.phonebook.viewmodels.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import rx.android.schedulers.AndroidSchedulers
import java.lang.NumberFormatException
import java.util.concurrent.TimeUnit

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
        model.shownContacts.observe(this, Observer { contacts ->
            Log.d(TAG, "Shown contacts was updated")

            if(contacts.isNullOrEmpty()) {
                main_placeholder.visibility = View.VISIBLE
            } else {
                main_placeholder.visibility = View.INVISIBLE
            }

            if(contacts == null)
                adapter.setData(emptyList())
            else
                adapter.setData(contacts)

            adapter.notifyDataSetChanged()

        })
    }

    //
    // Toolbar / Menu functions
    //

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchBar : SearchView = menu?.findItem(R.id.main_menu_search)?.actionView as SearchView
        RxSearchView.queryTextChanges(searchBar)
            .debounce(1000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .subscribe {term ->
                search(term.toString())
            }

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

    fun search(term : String) {
        Log.d(TAG, "Searching term: $term")

        // prevents accidental (any) whitespace from hiding search
        if(term.matches(Regex("\\w*"))){
            model.resetSearch()
        }

        if(Contact.isValidEmail(term)) {
            Log.d(TAG, "Found valid email: $term")
            Log.d(TAG, "Records: ${ContactDB.getDao().getByEmail(term).value}")
            model.updateShownContacts(model.repo.getByEmail(term))
            return
        }
        if(Contact.isValidPhoneNumber(term)) {
            Log.d(TAG, "Found valid phonenum: $term")
            model.updateShownContacts(model.repo.getByPhoneNumber(term))
            return
        }

    }

    // encapsulates the process of launching contact details, meaning a state will be required
    private fun launchContactDetails(state : EditDetailsActivity.State, contact: Contact? = null) {
        val intent = Intent(this, EditDetailsActivity::class.java)
        intent.putExtra("state", state)

        if(contact != null) {
            intent.putExtra("contact", contact)
        }

        startActivity(intent)
    }

}
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
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding.widget.RxSearchView
import com.scullyapps.phonebook.adapters.ContactsRecyclerAdapter
import com.scullyapps.phonebook.data.ContactDB
import com.scullyapps.phonebook.models.Contact
import com.scullyapps.phonebook.viewmodels.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import rx.android.schedulers.AndroidSchedulers
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

        model.shownContacts.observe(this, Observer { contacts ->
            // if null or empty, show placeholder
            setPlaceholderVisible(contacts.isNullOrEmpty())

            // alias model variables to defaults
            val searching = model.isSearching.value ?: false
            val searchTerm = model.searchTerm.value ?: ""

            var searchResultText = getString(R.string.search_result_header_untermed, contacts.size)

            // Set 'found 8 results' or 'found 8 results for X'
            if(searching && searchTerm.isNotEmpty())
                searchResultText = getString(R.string.search_result_header_termed, contacts.size, model.searchTerm.value)

            txt_main_searchresults.text = searchResultText

            // if null, send empty list
            adapter.setData(contacts ?: emptyList())
            adapter.notifyDataSetChanged()
        })
    }

    override fun onResume() {
        // when we arrive back at the activity, reload contacts from database
        model.loadAllContacts()
        super.onResume()
    }

    //
    // Toolbar / Menu functions
    //

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        // If text in search bar changes, call upon the search function
        val searchBar : SearchView = menu?.findItem(R.id.main_menu_search)?.actionView as SearchView
        RxSearchView.queryTextChanges(searchBar)
            .debounce(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .skip(1 )
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

    // set our placeholder text + visibility
    private fun setPlaceholderVisible(visible : Boolean) {
        if(model.isSearching.value == true)
            txt_main_placeholder.text = getString(R.string.placeholder_text_searching)
        else
            txt_main_placeholder.text = getString(R.string.placeholder_text)

        if(visible) {
            main_placeholder.visibility = View.VISIBLE
        } else {
            main_placeholder.visibility = View.GONE
        }
    }

    private fun search(term : String) {
        Log.d(TAG, "Searching term: [$term]")

        // prevents accidental (any) whitespace from hiding search
        if(term.matches(Regex("^\\s*\$")) || term.isEmpty()){
            model.resetSearch()
        }

        model.searchTerm.value = term

        var newContacts : List<Contact>? = emptyList()

        // switchout contacts we send to be shown

        newContacts = if(Contact.isValidEmail(term)) {
            model.repo.getByEmail(term)
        } else if (Contact.isValidPhoneNumber(term)) {
            model.repo.getByPhoneNumber(term)
        } else {
            model.repo.getByFullName(term)
        }

        model.apply {
            updateShownContacts(newContacts)
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
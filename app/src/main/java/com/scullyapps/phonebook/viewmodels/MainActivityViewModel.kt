package com.scullyapps.phonebook.viewmodels


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.scullyapps.phonebook.data.ContactRepository
import com.scullyapps.phonebook.models.Contact

class MainActivityViewModel : ViewModel() {
    private val TAG: String = "MainActivityViewModel"

    val repo: ContactRepository = ContactRepository()

    // these are what are actually shown to the user
    var shownContacts = MutableLiveData<List<Contact>>()

    var isSearching = MutableLiveData<Boolean>(false)
    var searchTerm = MutableLiveData<String>("")

    init {
        updateShownContacts(repo.getAllContacts())
    }

    fun loadAllContacts() {
        val list = repo.getAllContacts()
        Log.d(TAG, "Found list of contacts: $list")
        updateShownContacts(list)
    }

    fun resetSearch() {
        isSearching.value = false
        searchTerm.value = ""
        loadAllContacts()
    }

    // code before searching/updating can go here
    fun updateShownContacts(list : List<Contact>?) {
        shownContacts.value = list
    }
}
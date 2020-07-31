package com.scullyapps.phonebook.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.scullyapps.phonebook.data.ContactRepository
import com.scullyapps.phonebook.models.Contact

class MainActivityViewModel : ViewModel() {
    private val TAG: String = "MainActivityViewModel"

    val repo: ContactRepository = ContactRepository()

    // keep a copy of all contacts regardless of search
    private val allContacts : LiveData<List<Contact>>

    // these are what are actually shown to the user
    var shownContacts = MutableLiveData<List<Contact>>()


    init {
        allContacts = repo.contacts
        updateShownContacts(repo.getAllContacts())
    }

    fun resetSearch() {
        updateShownContacts(repo.getAllContacts())
    }

    // code before searching/updating can go here
    fun updateShownContacts(list : List<Contact>?) {
        shownContacts.postValue(list)
    }
}
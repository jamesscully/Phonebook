package com.scullyapps.phonebook.viewmodels


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.scullyapps.phonebook.data.ContactRepository
import com.scullyapps.phonebook.models.Contact

class MainActivityViewModel : ViewModel() {
    private val TAG: String = "MainActivityViewModel"

    private val repo: ContactRepository = ContactRepository()
    val contacts : LiveData<List<Contact>>

    init {
        contacts = repo.contacts
    }
}
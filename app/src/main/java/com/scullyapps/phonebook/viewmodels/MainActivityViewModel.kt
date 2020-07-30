package com.scullyapps.phonebook.viewmodels


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.scullyapps.phonebook.models.Contact

class MainActivityViewModel : ViewModel() {
    private val TAG: String = "MainActivityViewModel"

    var contacts : LiveData<List<Contact>> = MutableLiveData<List<Contact>>()
}
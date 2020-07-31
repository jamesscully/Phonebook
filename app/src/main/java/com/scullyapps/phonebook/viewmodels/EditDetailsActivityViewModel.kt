package com.scullyapps.phonebook.viewmodels


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.scullyapps.phonebook.EditDetailsActivity
import com.scullyapps.phonebook.models.Contact

class EditDetailsActivityViewModel : ViewModel() {
    private val TAG: String = "EditDetailsActivityViewModel"

    // incoming contact from intent; use buildContact() to retrieve updated info
    var contact: Contact? = null

    // we can assume viewing by default - this is not "destructive"
    var state = MutableLiveData<EditDetailsActivity.State>().apply {
        value = EditDetailsActivity.State.VIEWING
    }

    var firstName   = MutableLiveData<String>("")
    var secondName  = MutableLiveData<String>("")
    var email       = MutableLiveData<String>("")
    var phone       = MutableLiveData<String>("")
    var address     = MutableLiveData<String>("")

    var emailValid : Boolean = false
    var phoneValid : Boolean = false

    fun loadContact(c : Contact) {
        contact = c
        firstName.postValue(c.firstName)
        secondName.postValue(c.secondName)
        email.postValue(c.email)
        phone.postValue(c.phone)
        address.postValue(c.address)
    }

    fun buildContact(): Contact {
        return Contact(firstName.value!!, secondName.value!!, email.value!!, phone.value!!, address.value)
    }

}
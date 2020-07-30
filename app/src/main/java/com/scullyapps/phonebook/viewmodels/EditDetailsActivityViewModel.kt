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

    var firstName : String = ""
    var secondName : String = ""
    var email : String = ""
    var phone : String = ""
    var address : String = ""

    fun buildContact(): Contact {
        return Contact(firstName, secondName, email, phone, address)
    }

}
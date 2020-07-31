package com.scullyapps.phonebook.data


import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import com.scullyapps.phonebook.models.Contact

class ContactRepository {
    private val TAG: String = "ContactRepository"

    val contacts : LiveData<List<Contact>> = ContactDB.getDao().getAll()

//    fun getAllContacts() : List<Contact>? {
//        return getAllContactsTask().execute().get()
//    }
//
//    fun getContactById(id : Int) : Contact {
//        return getContactByIdTask(id).execute().get()
//    }
//
//    companion object {
//        private val db : ContactDB = ContactDB.getInstance()
//
//        private class getAllContactsTask : AsyncTask<Void, Void, List<Contact>>() {
//            override fun doInBackground(vararg p0: Void?): List<Contact> {
//                return db.contactDAO().getAll()
//            }
//        }
//
//        private class getContactByIdTask(private val id : Int) : AsyncTask<Void, Void, Contact>() {
//            override fun doInBackground(vararg p0: Void?): Contact {
//                return db.contactDAO().getById(id = this.id)
//            }
//        }
//    }


}
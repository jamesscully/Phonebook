package com.scullyapps.phonebook.data


import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.scullyapps.phonebook.models.Contact

class ContactRepository {
    private val TAG: String = "ContactRepository"

    fun getByPhoneNumber(number: String) : List<Contact>? {
        return GetByPhoneNumberTask(number).execute().get()
    }

    fun getByEmail(email: String) : List<Contact>? {
        return GetByEmailTask(email).execute().get()
    }

    fun getAllContacts() : List<Contact>? {
        return GetAllContactsTask().execute().get()
    }

    fun getByFullName(fullName: String) : List<Contact>? {
        return GetByFullNameTask("%${fullName}%").execute().get()
    }

    companion object {
        private val db : ContactDB = ContactDB.getInstance()

        var SORT_ASCENDING = true

        private class GetAllContactsTask : AsyncTask<Boolean, Void, List<Contact>>() {
            override fun doInBackground(vararg p0: Boolean?): List<Contact> {
                return if(SORT_ASCENDING) {
                    db.contactDAO().getAllList()
                } else
                    db.contactDAO().getAllDescList()
            }
        }

        private class GetByEmailTask(private val email: String) : AsyncTask<Void, Void, List<Contact>>() {
            override fun doInBackground(vararg p0: Void?): List<Contact> {
                return db.contactDAO().getByEmailList(email)
            }
        }

        private class GetByPhoneNumberTask(private val number: String) : AsyncTask<Void, Void, List<Contact>>() {
            override fun doInBackground(vararg p0: Void?): List<Contact> {
                    return db.contactDAO().getByPhoneNum(number)
            }
        }

        private class GetByFullNameTask(private val fullName: String) : AsyncTask<Void, Void, List<Contact>>() {
            override fun doInBackground(vararg p0: Void?): List<Contact> {
                return db.contactDAO().getByFullName(fullName)
            }
        }

    }
}
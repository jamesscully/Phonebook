package com.scullyapps.phonebook

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.scullyapps.phonebook.data.ContactDB
import com.scullyapps.phonebook.models.Contact
import com.scullyapps.phonebook.models.ContactDAO
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ContactDatabaseTest {

    private val TAG = "ContactDatabaseTest"

    private lateinit var dao: ContactDAO
    private lateinit var db: ContactDB

    @Before
    fun setup() {
        // use the test variant of our db
        db = ContactDB.getTestInstance()
        dao = db.contactDAO()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
        db.clearAllTables()
    }


    @Test
    fun insertionTest() {
        val contact = Contact("Test", "Name", "Test@gmail.com", "0782725502", "25 Road")

        dao.insert(contact)

        val byName = dao.getByName("Test", "Name")

        Log.d("ContactDatabaseTest", "Testing... created: ${contact} \n retrieved ${byName}")

        // Contacts.equals compares the _id of new and old, but we only need to check it's created.
        // Tables are wiped after each test (auto-increment is not)
        assertNotNull(byName)
    }

    @Test
    fun deleteByName() {
        val first = "Test"
        val second = "Name"

        val ref = dao.getByName(first, second)

        Log.d(TAG, "Found contact: $ref")
    }
}
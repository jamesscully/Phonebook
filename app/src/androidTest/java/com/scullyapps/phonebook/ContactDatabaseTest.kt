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

        val contactA = Contact(first, second, "Test@gmail.com", "0782725502", "25 Road")
        val contactB = Contact(first, "Bloggs", "Test@gmail.com", "0782725502", "25 Road")

        dao.insert(contactA, contactB)

        val ref = dao.getByName(first, second)

        dao.delete(ref)

        // test that we have deleted contact A
        assertNull(dao.getByName(first, second))

        // test that contact B is not deleted (similar last names)
        assertNotNull(dao.getByName(first, "Bloggs"))
    }

    @Test
    fun getAllTest() {
        dao.insert(Contact("Test", "Name", "test@gmail.com", "0000000000", "address"))
        dao.insert(Contact("Test", "Name", "test@gmail.com", "0000000000", "address"))
        dao.insert(Contact("Test", "Name", "test@gmail.com", "0000000000", "address"))

        assertEquals(3, dao.getAllList().size)

        dao.insert(Contact("Test", "Name", "test@gmail.com", "0000000000", "address"))
        dao.insert(Contact("Test", "Name", "test@gmail.com", "0000000000", "address"))

        assertEquals(5, dao.getAllList().size)
    }

    @Test
    fun getAllSortedTest() {

        val contactA = Contact("Aberdeen", "Buckingham", "test@gmail.com", "07827275818", "Address")
        val contactC = Contact("Coventry", "Zelda", "test@gmail.com", "07827275818", "Address")
        val contactB = Contact("Buckingham", "Zelda", "test@gmail.com", "07827275818", "Address")

        dao.insert(contactA, contactB, contactC)

        var entries = dao.getAllList()

        Log.d(TAG, "Found entries (${entries.size}): \n $entries")

        assertTrue(contactA.equalByData(entries[0]))
        assertTrue(contactB.equalByData(entries[1]))
        assertTrue(contactC.equalByData(entries[2]))

        entries = dao.getAllDescList()

        assertTrue(contactA.equalByData(entries[2]))
        assertTrue(contactB.equalByData(entries[1]))
        assertTrue(contactC.equalByData(entries[0]))

    }
}
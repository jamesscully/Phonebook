package com.scullyapps.phonebook

import com.scullyapps.phonebook.models.Contact
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ContactValidationTest {
    @Test
    fun testValidNumber() {
        assertTrue(Contact.isValidPhoneNumber("07827275818"))

        // Test spaces
        assertTrue(Contact.isValidPhoneNumber("0115 947 5337"))
        assertTrue(Contact.isValidPhoneNumber("078 27 275 81 8"))

        assertFalse(Contact.isValidPhoneNumber("07827ab5818"))
        assertFalse(Contact.isValidPhoneNumber("abcd1234567"))
    }

    @Test
    fun testValidEmail() {

    }
}
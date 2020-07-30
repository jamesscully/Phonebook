package com.scullyapps.phonebook.models


import android.util.Log

class Contact {
    private val TAG: String = "Contact"


    companion object {
        fun isValidPhoneNumber(num : String) : Boolean {

            var phone = num

            // remove any spaces from num
            phone = num.replace(Regex("\\s+"), "")

            // numbers should have 11 digits i.e. 0 78272 75818 (the 0 should be added prior, since we can't modify here)
            if(phone.length != 11)
                return false

            // should begin with a 0
            if(phone[0] != '0')
                return false

            // check if line is only numerics
            return phone.matches(Regex("^[0-9]*$"))
        }
    }
}
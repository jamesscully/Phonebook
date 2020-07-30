package com.scullyapps.phonebook.models


import android.util.Log
import android.util.Patterns
import androidx.core.util.PatternsCompat
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.regex.Matcher

@Entity
data class Contact(
    @PrimaryKey val cid : Int,
    @ColumnInfo(name="first_name") val firstName: String,
    @ColumnInfo(name="second_name") val secondName: String,
    @ColumnInfo(name="email") val email: String,
    @ColumnInfo(name="phone") val phone: String,
    @ColumnInfo(name="address") val address: String?
) {
    @Ignore
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

        fun isValidEmail(e : String) : Boolean {
             return e.matches(PatternsCompat.EMAIL_ADDRESS.toRegex())
        }
    }
}
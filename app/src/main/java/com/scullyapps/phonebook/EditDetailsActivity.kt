package com.scullyapps.phonebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class EditDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_details)

        // if we're being sent a contact, load details + 'edit'-themed UI,
        if(intent.hasExtra("contact")) {

        }
    }
}
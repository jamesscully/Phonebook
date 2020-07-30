package com.scullyapps.phonebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.scullyapps.phonebook.models.Contact
import com.scullyapps.phonebook.viewmodels.EditDetailsActivityViewModel
import com.scullyapps.phonebook.viewmodels.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_edit_details.*

class EditDetailsActivity : AppCompatActivity() {

    enum class State {
        VIEWING,
        EDITING,
        CREATING
    }

    private val model : EditDetailsActivityViewModel by viewModels<EditDetailsActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_details)

        model.state.observe(this, Observer { state ->
            when(state) {
                State.VIEWING -> {

                }

                State.EDITING -> {

                }

                State.CREATING -> {

                }
            }
        })

        // if we're being sent a contact, load details + 'edit'-themed UI,
        if(intent.hasExtra("contact")) {
            model.contact = intent.getSerializableExtra("contact") as Contact
            fillForm()
            enableEditing(true)
        }
    }

    /*
        Loads contact details from our model's contact
     */
    fun fillForm() {
        etxt_edit_firstname.setText(model.contact?.firstName)
        etxt_edit_secondname.setText(model.contact?.secondName)
        etxt_edit_email.setText(model.contact?.email)
        etxt_phonenumber.setText(model.contact?.phone)
        etxt_edit_address.setText(model.contact?.address)
    }

    fun enableEditing(lock : Boolean) {
        etxt_edit_firstname.isEnabled = lock
        etxt_edit_secondname.isEnabled = lock
        etxt_edit_email.isEnabled = lock
        etxt_phonenumber.isEnabled = lock
        etxt_edit_address.isEnabled = lock
    }
}
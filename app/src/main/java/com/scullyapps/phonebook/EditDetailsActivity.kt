package com.scullyapps.phonebook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.viewModels
import androidx.core.util.PatternsCompat
import androidx.lifecycle.Observer
import com.jakewharton.rxbinding.widget.RxTextView
import com.scullyapps.phonebook.data.ContactDB
import com.scullyapps.phonebook.models.Contact
import com.scullyapps.phonebook.viewmodels.EditDetailsActivityViewModel
import com.scullyapps.phonebook.viewmodels.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_edit_details.*
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Func1
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class EditDetailsActivity : AppCompatActivity() {

    private val TAG = "EditDetailsActivity"

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
                    enableEditing(false)
                    btn_process.text = "Viewing"
                    setToolbarText("Viewing ${model.contact?.firstName} } ${model.contact?.secondName} ")
                }

                State.EDITING -> {
                    enableEditing(true)
                    btn_process.text = getString(R.string.form_btn_update)
                    setToolbarText("Updating ${model.contact?.firstName}")
                }

                State.CREATING -> {
                    enableEditing(true)
                    btn_process.text = getString(R.string.form_btn_create)
                    setToolbarText("Creating new contact")
                }

                null -> {
                    // todo handle null
                }
            }
        })

        btn_process.setOnClickListener {
            process()
        }

        model.state.postValue(intent.getSerializableExtra("state") as State)

        // if we're being sent a contact, load details + 'edit'-themed UI,
        if(intent.hasExtra("contact")) {
            model.loadContact(intent.getSerializableExtra("contact") as Contact)
            fillForm()
        }

        setupObservers()
    }

    private fun setToolbarText(s : String) {
        val toolbar = supportActionBar
        toolbar?.let { title = s }
    }

    private fun setupObservers() {
        // Unvalidated - simply write to model
        RxTextView.textChanges(etxt_edit_firstname).subscribe { s -> model.firstName.postValue(s.toString()) }
        RxTextView.textChanges(etxt_edit_secondname).subscribe { s -> model.secondName.postValue(s.toString()) }
        RxTextView.textChanges(etxt_edit_address).subscribe { s -> model.address.postValue(s.toString()) }

        RxTextView.textChanges(etxt_edit_email)
            .debounce(1000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .subscribe({ s ->
                // set validity flag in model
                model.emailValid = Contact.isValidEmail(s.toString())

                if(model.emailValid) {
                    model.email.postValue(s.toString())
                } else {
                    etxt_edit_email.error = getString(R.string.error_email)
                }

                formValidation()
            }, {t: Throwable? -> Log.e(TAG, t.toString())}
            )

        RxTextView.textChanges(etxt_phonenumber)
            .debounce(1000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .subscribe({s ->
                model.phoneValid = Contact.isValidPhoneNumber(s.toString())
                if(model.phoneValid) {
                    model.phone.postValue(s.toString())
                } else {
                    etxt_phonenumber.error = getString(R.string.error_phone)
                }
                formValidation()
            }, {t: Throwable? -> Log.e(TAG, t.toString())})

    }

    private fun process() {
        val contact : Contact = model.buildContact()

        when(model.state.value) {
            State.EDITING -> {
                // update
                ContactDB.update(contact)
            }

            State.CREATING -> {
                ContactDB.insert(contact)
            }

            State.VIEWING -> {
                Log.w(TAG, "Attempt to save/update contact when in viewing mode?")
            }
        }

        Log.d(TAG, "Built contact: \n ${model.buildContact()}")
    }

    // trigger UI events if we're valid
    private fun formValidation() {

        // add flags for validity here
        val formValid = (model.phoneValid && model.emailValid)

        // for now we only need a simple button disable/enable
        btn_process.isEnabled = formValid
    }

    private fun fillForm() {
        etxt_edit_firstname.setText(model.contact?.firstName)
        etxt_edit_secondname.setText(model.contact?.secondName)
        etxt_edit_email.setText(model.contact?.email)
        etxt_phonenumber.setText(model.contact?.phone)
        etxt_edit_address.setText(model.contact?.address)
    }

    private fun enableEditing(lock : Boolean) {
        etxt_edit_firstname.isEnabled = lock
        etxt_edit_secondname.isEnabled = lock
        etxt_edit_email.isEnabled = lock
        etxt_phonenumber.isEnabled = lock
        etxt_edit_address.isEnabled = lock

        btn_process.isEnabled = lock
    }
}
package com.scullyapps.phonebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.util.PatternsCompat
import androidx.lifecycle.Observer
import com.jakewharton.rxbinding.widget.RxTextView
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
                }

                State.EDITING -> {
                    enableEditing(true)
                    btn_process.text = "Update"
                }

                State.CREATING -> {
                    enableEditing(true)
                    btn_process.text = "Create"
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

        RxTextView.textChanges(etxt_edit_email)
            .debounce(1000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .subscribe({ s ->
            if(Contact.isValidEmail(s.toString())) {
                model.email.postValue(s.toString())
            } else {
                etxt_edit_email.error = getString(R.string.error_email)
            }
        }, {t: Throwable? -> Log.e(TAG, t.toString())})

        RxTextView.textChanges(etxt_phonenumber)
            .debounce(1000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .subscribe({s ->
            if(Contact.isValidPhoneNumber(s.toString())) {
                model.phone.postValue(s.toString())
            } else {
                etxt_phonenumber.error = getString(R.string.error_phone)
            }
        }, {t: Throwable? -> Log.e(TAG, t.toString())})
    }

    private fun process() {
        // is the form valid to be updated/created?
        if(!formValidation())
            return

        Log.d(TAG, "Built contact: \n ${model.buildContact()}")
    }

    private fun formValidation() : Boolean {
        if(!Contact.isValidEmail(etxt_edit_email.text.toString())) {
            // error in email
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_LONG).show()
            return false
        }

        if(!Contact.isValidPhoneNumber(etxt_phonenumber.text.toString())) {
            // error in phone
            Toast.makeText(this, "Please enter a valid UK phone number", Toast.LENGTH_LONG).show()
            return false
        }

        // If we've passed the above checks, then we're valid.
        // Room will prevent against SQL injection
        return true
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
    }
}
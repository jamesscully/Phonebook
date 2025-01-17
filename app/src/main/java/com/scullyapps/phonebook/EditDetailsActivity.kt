package com.scullyapps.phonebook

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.jakewharton.rxbinding.widget.RxTextView
import com.scullyapps.phonebook.data.ContactDB
import com.scullyapps.phonebook.models.Contact
import com.scullyapps.phonebook.viewmodels.EditDetailsActivityViewModel
import kotlinx.android.synthetic.main.activity_edit_details.*
import rx.android.schedulers.AndroidSchedulers
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

        setupStateObserver()

        btn_process.setOnClickListener {
            process()
        }

        // only set state if we're a fresh activity
        if(model.state.value == null)
            model.state.postValue(intent.getSerializableExtra("state") as State)

        // if we're being sent a contact, load details + 'edit'-themed UI,
        if(intent.hasExtra("contact") && model.contact == null) {
            model.loadContact(intent.getSerializableExtra("contact") as Contact)
            fillForm()
        }

        setupObservers()
    }



    private fun setupStateObserver() {
        model.state.observe(this, Observer { state ->
            Log.d(TAG, "State changed: $state")
            when(state) {
                State.VIEWING -> {
                    enableEditing(false)
                    btn_process.text = "Viewing"
                    btn_process.visibility = View.INVISIBLE
                    setToolbarText("Viewing ${model.contact?.fullName}")
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
    }
    private fun setupObservers() {
        // Unvalidated - simply write to model
        RxTextView.textChanges(etxt_edit_firstname).subscribe { s -> model.firstName.postValue(s.toString()) }
        RxTextView.textChanges(etxt_edit_secondname).subscribe { s -> model.secondName.postValue(s.toString()) }
        RxTextView.textChanges(etxt_edit_address).subscribe { s -> model.address.postValue(s.toString()) }

        RxTextView.textChanges(etxt_edit_email)
            .debounce(400, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .subscribe({ s ->
                // set validity flag in model
                model.emailValid = Contact.isValidEmail(s.toString())

                Log.d(TAG, "[${s.toString()}]")

                if(model.emailValid || s.isEmpty()) {
                    model.email.value = s.toString()
                } else {
                    etxt_edit_email.error = getString(R.string.error_email)
                }

                formValidation()
            }, {t: Throwable? -> Log.e(TAG, t.toString())})

        RxTextView.textChanges(etxt_phonenumber)
            .debounce(400, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .subscribe({s ->
                model.phoneValid = Contact.isValidPhoneNumber(s.toString())
                if(model.phoneValid || s.isEmpty()) {
                    model.phone.value = s.toString()
                } else {
                    etxt_phonenumber.error = getString(R.string.error_phone)
                }

                formValidation()
            }, {t: Throwable? -> Log.e(TAG, t.toString())})

    }

    private fun process() {
        val contact : Contact = model.buildContact()

        when(model.state.value) {
            State.EDITING -> ContactDB.update(contact)
            State.CREATING -> {
                ContactDB.insert(contact)
                // Requirement
                finish()
            }
            else -> {
                Log.w(TAG, "Attempt to save/update when in viewing mode?")
            }
        }

        Toast.makeText(this, getString(R.string.toast_saved_contact, contact.fullName), Toast.LENGTH_SHORT).show()

        // update model variables, back to viewmode
        model.contact = contact
        setUiState(State.VIEWING)

        Log.d(TAG, "Built contact: \n ${model.buildContact()}")
    }

    override fun onBackPressed() {
        Log.d(TAG, "Back pressed, edited: ${model.edited}")

        when(model.state.value) {
            State.VIEWING -> super.onBackPressed()
            State.EDITING -> {
                if(model.edited)
                    onUnsavedChanges(false)
                else
                    setUiState(State.VIEWING)
            }
            State.CREATING -> onUnsavedChanges(true)
        }
    }

    /*
        There are only two states for this to fire from -
        - Exiting edit mode (go to view mode)
        - Creation of contact (leave activity)
     */
    private fun onUnsavedChanges(exitActivity : Boolean = false) {
        val dialog = DataWarningDialog(this)

        dialog.setPositiveButton("Abandon") { d, i ->
            if(exitActivity) {
                finish()
            } else {
                // load as if we were viewing
                fillForm()
                setUiState(State.VIEWING)
            }
        }

        dialog.create().show()
    }

    // On deletion of this contact
    private fun onDelete() {
        val dialog = DataWarningDialog(this)
            .setMessage("Are you sure you wish to delete ${model.firstName.value}?\nThis action cannot be undone!")
            .setPositiveButton("Delete") {d, i ->
                if(model.contact != null)
                    ContactDB.delete(model.contact!!)
                finish()
            }
        dialog.create().show()
    }

    //
    // Menu overrides
    //

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.contact_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_contact_delete -> {
                onDelete()
            }

            R.id.menu_contact_edit -> {
                if(model.state.value == State.VIEWING) {
                    setUiState(State.EDITING)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    //
    // Form functions
    //

    // trigger UI events if we're valid
    private fun formValidation() {
        // add flags for validity here
        val formValid = (model.phoneValid && model.emailValid)

        btn_process.setVisible(formValid)
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

        btn_process.setVisible(lock)
    }

    //
    // Helpers
    //

    private fun setUiState(state : State) {
        model.state.value = state
    }

    // Extension to get rid of these lines of code
    private fun View.setVisible(b: Boolean) {
        if(b) {
            this.visibility = View.VISIBLE
        } else {
            this.visibility = View.INVISIBLE
        }
    }

    private fun setToolbarText(s : String) {
        val toolbar = supportActionBar
        toolbar?.let { title = s }
    }

    inner class DataWarningDialog(context: Context) : AlertDialog.Builder(context) {
        init {
            setCancelable(false)
            setTitle("Unsaved Changes")
            setMessage("Are you sure you wish to abandon these changes?")
            setNegativeButton("Cancel") { d, _ ->
                d.cancel()
            }
        }
    }
}
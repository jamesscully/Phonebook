package com.scullyapps.phonebook.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.scullyapps.phonebook.R
import com.scullyapps.phonebook.models.Contact

class ContactsRecyclerAdapter(private val listener: (Contact) -> Unit) : RecyclerView.Adapter<ContactsRecyclerAdapter.ViewHolder>() {
    private val TAG: String = "ContactsRecyclerAdapter"

    private var dataset = emptyList<Contact>()

    fun setData(contacts : List<Contact>) {
        dataset = contacts
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_contact, parent, false)
        val vh = ViewHolder(view)

        return vh
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = dataset[position]
        holder.txtName.text = (contact.firstName + " " +  contact.secondName)

        // fires the lambda we passed to the adapter, so that we can easily capture onclick
        holder.itemView.setOnClickListener {
            listener.invoke(contact)
        }
    }

    inner class ViewHolder(vh : View) : RecyclerView.ViewHolder(vh) {
        val txtName : TextView = vh.findViewById(R.id.txt_contactview)
    }
}
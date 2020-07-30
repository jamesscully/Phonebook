package com.scullyapps.phonebook.models


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ContactDAO {
    @Query("SELECT * FROM contact")
    fun getAll(): LiveData<List<Contact>>

    @Query("SELECT * FROM contact WHERE first_name LIKE :first AND second_name LIKE :second")
    fun getByName(first : String, second : String) : Contact

    @Query("SELECT * FROM contact WHERE phone=:number")
    fun getByPhoneNum(number : String) : Contact

    @Query("SELECT * FROM contact WHERE email=:email")
    fun getByEmail(email : String) : Contact

    @Insert
    fun insert(vararg contacts: Contact)

    @Delete
    fun delete(contact: Contact)
}
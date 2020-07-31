package com.scullyapps.phonebook.models


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface ContactDAO {
    @Query("SELECT * FROM contact ORDER BY first_name ASC")
    fun getAll(): LiveData<List<Contact>>

    @Query("SELECT * FROM contact ORDER BY first_name DESC")
    fun getAllDesc(): LiveData<List<Contact>>

    // Used for unit testing
    @Query("SELECT * FROM contact ORDER BY first_name ASC")
    fun getAllList(): List<Contact>

    @Query("SELECT * FROM contact ORDER BY first_name DESC")
    fun getAllDescList(): List<Contact>
    //

    @Query("SELECT * FROM contact WHERE cid=:id")
    fun getById(id : Int) : Contact

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

    @Update
    fun update(contact: Contact)
}
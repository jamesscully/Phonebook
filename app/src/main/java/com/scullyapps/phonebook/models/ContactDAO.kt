package com.scullyapps.phonebook.models


import androidx.lifecycle.LiveData
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
    fun getById(id : Int) : LiveData<List<Contact>>

    @Query("SELECT * FROM contact WHERE first_name LIKE :first AND second_name LIKE :second")
    fun getByName(first : String, second : String) : Contact

    @Query("SELECT * FROM contact WHERE  (first_name || ' ' ||second_name) LIKE :fullName")
    fun getByFullName(fullName: String) : List<Contact>

    @Query("SELECT * FROM contact WHERE phone=:number")
    fun getByPhoneNum(number : String) : List<Contact>

    @Query("SELECT * FROM contact WHERE email=:email")
    fun getByEmail(email : String) : LiveData<List<Contact>>

    @Query("SELECT * FROM contact WHERE email=:email")
    fun getByEmailList(email : String) : List<Contact>

    @Insert
    fun insert(vararg contacts: Contact)

    @Delete
    fun delete(contact: Contact)

    @Update
    fun update(contact: Contact)
}
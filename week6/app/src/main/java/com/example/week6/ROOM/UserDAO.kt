package com.example.week6.ROOM

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy

@Dao
interface UserDAO {
    @Query("SELECT * FROM user ")
    fun getAll():List<User>

    @Query("SELECT * FROM user WHERE name LIKE :name")
    fun findByName(name: String) :User

    @Query("SELECT * FROM user WHERE id =:id")
    fun findbyId(id: Int) :User

    @Insert
    fun insertAll(vararg todo: User) : List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(obj: User): Long

    @Delete
    fun delete(todo: User)

    @Update
    fun update( name: User)

    @Query("DELETE FROM user")
    fun deleteAllUser()



}
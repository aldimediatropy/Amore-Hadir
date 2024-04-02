package com.setalis.amorehr.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.setalis.amorehr.data.entities.User

/**
 * Crafted by Al (ismealdi) on 08/04/23.
 * Mediatropy
 */
@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun user(): User

    @Query("DELETE FROM users")
    suspend fun logout()
}
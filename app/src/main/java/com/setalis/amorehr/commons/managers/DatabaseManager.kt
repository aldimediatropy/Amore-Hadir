package com.setalis.amorehr.commons.managers

import androidx.room.Database
import androidx.room.RoomDatabase
import com.setalis.amorehr.data.dao.UserDao
import com.setalis.amorehr.data.entities.User

/**
 * Crafted by Al (ismealdi) on 08/04/23.
 * Mediatropy
 */
@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class DatabaseManager : RoomDatabase() {
    abstract val userDao: UserDao
}

fun userDao(dataBase: DatabaseManager): UserDao {
    return dataBase.userDao
}
package com.wipro.facts.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Facts::class], version = 1)
abstract class FactsDatabase : RoomDatabase() {
    abstract fun factsDao(): FactsDao
}

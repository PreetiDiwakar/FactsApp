package com.wipro.facts.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FactsDao {

    // Query to fetch all the data from the
    // SQLite database
    // No need of suspend method here
    @Query("SELECT * FROM facts")
    // Kotlin flow is an asynchronous stream of values
    fun getAllFacts(): Flow<List<Facts>>


    // If a new data is inserted with same primary key
    // It will get replaced by the previous one
    // This ensures that there is always a latest
    // data in the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    // The fetching of data should NOT be done on the
    // Main thread. Hence coroutine is used
    // If it is executing on one one thread, it may suspend
    // its execution there, and resume in another one
    suspend fun insertFacts(facts: List<Facts>)

    // Once the device comes online, the cached data
    // need to be replaced, i.e. delete it
    // Again it will use coroutine to achieve this task
    @Query("DELETE FROM facts")
    suspend fun deleteAllFacts()
}
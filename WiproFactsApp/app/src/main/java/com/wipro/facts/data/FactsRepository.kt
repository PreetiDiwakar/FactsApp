package com.wipro.facts.data

import androidx.room.withTransaction
import com.wipro.facts.api.FactsAPI
import com.wipro.facts.util.networkBoundResource
import kotlinx.coroutines.delay
import org.jetbrains.annotations.NotNull
import javax.inject.Inject

class FactsRepository @Inject constructor(
    private val api: FactsAPI,
    private val db: FactsDatabase
) {
    private val factsDao = db.factsDao()
    private var title: String = "Facts"
    @NotNull
    fun getFacts() = networkBoundResource(
        query = {
            factsDao.getAllFacts()
        },
        fetch = {
            title = api.getFactsList().title
            api.getFactsList().results
        },
        saveFetchResult = { FactList ->
            db.withTransaction {
                factsDao.deleteAllFacts()
                val list: MutableList<Facts> = ArrayList()
                for(fact in FactList){
                    if(fact.title != null){
                        fact.fact = title
                        list.add(fact)
                    }
                }
                factsDao.insertFacts(list)
            }
        }
    )

}
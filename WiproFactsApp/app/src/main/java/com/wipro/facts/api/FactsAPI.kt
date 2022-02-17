package com.wipro.facts.api

import com.wipro.facts.data.Facts
import com.wipro.facts.data.FactsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface FactsAPI {
    companion object{
       const val BASE_URL = "https://dl.dropboxusercontent.com/"
    }

    // url to get the Facts
    @GET("s/2iodh4vg0eortkl/facts.json")
    @Headers("Content-type: application/json")
    suspend fun getFactsList(): FactsResponse
}
package com.wipro.facts.data

import com.google.gson.annotations.SerializedName

data class FactsResponse (
    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("rows")
    val results: List<Facts>
)
package com.wipro.facts.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.annotation.Nullable

// Data Class to store the data
@Entity(tableName = "facts")
data class Facts(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @Nullable
    @ColumnInfo(name = "fact")
    var fact: String?,
    @Nullable
    @ColumnInfo(name = "title")
    val title: String?,
    @Nullable
    @ColumnInfo(name = "imageHref")
    val imageHref: String?,
    @Nullable
    @ColumnInfo(name = "description")
    val description: String?
)

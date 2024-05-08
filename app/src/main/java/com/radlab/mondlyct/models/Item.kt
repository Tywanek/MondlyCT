package com.radlab.mondlyct.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items_table")
data class Item(
    @PrimaryKey val id: String = "",
    @Embedded val attributes: Attributes = Attributes()
)
package com.radlab.mondlyct.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.radlab.mondlyct.models.Item

@Dao
interface CodeTaskDao {
    @Query("SELECT * FROM items_table")
    fun getAllItems(): List<Item>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllItems(items: List<Item>)

    @Delete
    suspend fun delete(item: Item)
}

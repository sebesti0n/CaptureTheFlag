package com.tejasdev.repospect.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tejasdev.repospect.models.RiddleModel

@Dao
interface RiddleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRiddles(riddles: List<RiddleModel>)

    @Query("SELECT * FROM riddle_table ORDER BY level ASC")
    fun getRiddles(): List<RiddleModel>

    @Query("SELECT COUNT(*) FROM riddle_table")
    fun countRiddle(): Int

    @Query("DELETE FROM riddle_table")
    fun deleteTable()

}
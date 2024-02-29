package com.example.capturetheflag.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.capturetheflag.models.RiddleModel

@Dao
interface RiddleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRiddles(riddles: List<RiddleModel>)

    @Query("SELECT * FROM riddle_table ORDER BY level ASC")
    fun getRiddles(): List<RiddleModel>

    @Query("SELECT COUNT(*) FROM riddle_table")
    fun countRiddle(): Int

}
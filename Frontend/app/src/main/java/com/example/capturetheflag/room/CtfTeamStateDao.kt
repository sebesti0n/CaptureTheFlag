package com.example.capturetheflag.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.capturetheflag.models.Next

@Dao
interface CtfTeamStateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeamDetails(registeredTeam: Next)

    @Query("SELECT COUNT(*) FROM team_registration_table")
    suspend fun isTeamRegistered():Boolean

    @Query("SELECT Number_correct_answer FROM TEAM_REGISTRATION_TABLE WHERE team_id=:teamID")
    suspend fun getNextRiddleindex(teamID:Int):Int

}
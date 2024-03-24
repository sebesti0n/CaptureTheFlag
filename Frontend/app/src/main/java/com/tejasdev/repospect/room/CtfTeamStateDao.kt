package com.tejasdev.repospect.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tejasdev.repospect.models.Next

@Dao
interface CtfTeamStateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTeamDetails(registeredTeam: Next)

    @Query("SELECT COUNT(*) FROM team_registration_table")
    fun isTeamRegistered(): Int

    @Query("SELECT Number_correct_answer FROM TEAM_REGISTRATION_TABLE WHERE team_id=:teamID")
    fun getNextRiddleindex(teamID:Int):Int

}
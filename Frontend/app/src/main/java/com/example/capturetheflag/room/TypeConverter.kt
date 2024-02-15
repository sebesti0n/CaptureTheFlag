package com.example.capturetheflag.room

import androidx.room.TypeConverter
import com.example.capturetheflag.models.Next
import com.example.capturetheflag.models.RiddleModel
import com.google.gson.Gson

class TypeConverter {
    @TypeConverter
    fun fromRiddle(riddle: RiddleModel): String? {
        return riddle.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toRiddle(riddleJson: String): RiddleModel {
        return riddleJson.let {
            Gson().fromJson(it, RiddleModel::class.java)
        }
    }

    @TypeConverter
    fun fromNext(registeredTeam: Next): String? {
        return registeredTeam.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toNext(registeredTeamJson: String): Next {
        return registeredTeamJson.let {
            Gson().fromJson(it, Next::class.java)
        }
    }


}
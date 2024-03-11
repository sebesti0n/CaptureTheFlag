package com.sebesti0n.capturetheflag.room

import androidx.room.TypeConverter
import com.sebesti0n.capturetheflag.models.Next
import com.sebesti0n.capturetheflag.models.RiddleModel
import com.google.gson.Gson
import java.lang.StringBuilder

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
    
    @TypeConverter
    fun fromList(list: List<Integer>): String{
        var stringBuilder: StringBuilder = StringBuilder()
        for(ele in list) stringBuilder.append("$ele ")
        return stringBuilder.toString()
    }

    @TypeConverter
    fun toList(string: String): List<Int>{
        val array = string.split(" ")
        var list = arrayListOf<Int>()
        for(ele in array) list.add(ele.toInt())
        return list.toList()
    }

}
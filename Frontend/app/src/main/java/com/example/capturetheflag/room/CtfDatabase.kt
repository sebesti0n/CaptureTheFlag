package com.example.capturetheflag.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.capturetheflag.models.Next
import com.example.capturetheflag.models.RiddleModel

@Database(entities = [Next::class,RiddleModel::class], version = 2, exportSchema = false)
@TypeConverters(TypeConverter::class)
abstract class CtfDatabase : RoomDatabase() {
    abstract fun riddleDao(): RiddleDao
    abstract fun CtfTeamStateDao():CtfTeamStateDao
    companion object{

        @Volatile
        private var INSTANCE: CtfDatabase? = null



        fun getDatabase(context: Context): CtfDatabase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CtfDatabase::class.java,
                    "ctf_database"
                ).fallbackToDestructiveMigration()
                    .allowMainThreadQueries().build()
                INSTANCE =instance
                return instance
            }
        }

    }
}
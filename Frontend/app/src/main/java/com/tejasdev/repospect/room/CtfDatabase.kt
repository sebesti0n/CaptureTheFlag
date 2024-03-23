package com.tejasdev.repospect.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tejasdev.repospect.models.Next
import com.tejasdev.repospect.models.RiddleModel

@Database(entities = [Next::class,RiddleModel::class], version = 3, exportSchema = false)
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
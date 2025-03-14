package io.rapidz.assignment1.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.rapidz.assignment1.dao.AppDao
import io.rapidz.assignment1.data.Answer
import io.rapidz.assignment1.data.Candidate

@Database(entities=[Candidate::class, Answer::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
	abstract fun appDao() : AppDao
}
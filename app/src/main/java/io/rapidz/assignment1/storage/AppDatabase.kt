package io.rapidz.assignment1.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.rapidz.assignment1.dao.AnswerDao
import io.rapidz.assignment1.dao.CandidateDao
import io.rapidz.assignment1.data.Candidate

@Database(entities=[Candidate::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
	abstract fun candidateDao() : CandidateDao
	abstract fun answerDao(): AnswerDao

	companion object {
		@Volatile
		private var INSTANCE: AppDatabase? = null

		fun getDatabase(context: Context): AppDatabase {
			return INSTANCE ?: synchronized(this) {
				val instance = Room.databaseBuilder(
					context.applicationContext,
					AppDatabase::class.java,
					"app_database"
				).build()
				INSTANCE = instance
				instance
			}
		}
	}
}
package io.rapidz.assignment1.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.rapidz.assignment1.data.Timer

@Dao
interface TimerDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(timer: Timer)

	@Update
	suspend fun update(timer: Timer)

	@Query("SELECT * FROM timers WHERE candidateId = :candidateId LIMIT 1")
	suspend fun getTimerByCandidateId(candidateId: Long): Timer?

}
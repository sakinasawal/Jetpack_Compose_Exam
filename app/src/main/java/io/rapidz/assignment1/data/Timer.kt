package io.rapidz.assignment1.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timers")
data class Timer(
	@PrimaryKey(autoGenerate = false)
	val candidateId: Long,
	val remainingTime: Int
)


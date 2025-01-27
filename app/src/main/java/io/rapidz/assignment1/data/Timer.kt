package io.rapidz.assignment1.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timers")
data class Timer(
	@PrimaryKey(autoGenerate = true)
	val id: Long = 0,
	val candidateId: Long,
	val remainingTime: Int
)


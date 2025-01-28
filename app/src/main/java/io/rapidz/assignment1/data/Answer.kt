package io.rapidz.assignment1.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "answers")
data class Answer(
	@PrimaryKey(autoGenerate = true)
	val id: Int = 0,
	val questionId: Int,
	val answerText: String,
	val candidateId : Long,
	val defaultAnswer: String,
	val score: String,
	val remainingTime: Int
)

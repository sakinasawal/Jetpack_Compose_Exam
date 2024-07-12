package io.rapidz.assignment1.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
	tableName = "answer",
	foreignKeys = [ForeignKey(
		entity = Candidate::class,
		parentColumns = ["id"],
		childColumns = ["candidateId"],
		onDelete = ForeignKey.CASCADE
	)],
	indices = [Index(value = ["candidateId"])]
)
data class Answer(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	val candidateId: Long,
	val questionIndex: Int,
	val answer: String
)

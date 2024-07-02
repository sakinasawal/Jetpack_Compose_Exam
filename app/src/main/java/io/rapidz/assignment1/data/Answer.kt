package io.rapidz.assignment1.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "answer",
	foreignKeys = [ForeignKey(entity = Candidate::class, parentColumns = ["id"], childColumns = ["candidateId"])],
	indices = [Index(value=["candidateId"])]
)

data class Answer(
	@PrimaryKey(autoGenerate = true)
	val id : Int = 0,
	val candidateId : Int,
	val questionIndex : Int,
	val answerQuestion : String
)

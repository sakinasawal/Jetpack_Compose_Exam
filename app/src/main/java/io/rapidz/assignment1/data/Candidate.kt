package io.rapidz.assignment1.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "candidates")
data class Candidate(
	@PrimaryKey(autoGenerate = true)
	val id : Int = 0,
	val name: String,
	val emailAddress: String
)
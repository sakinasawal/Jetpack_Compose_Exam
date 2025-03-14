package io.rapidz.assignment1.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.rapidz.assignment1.utils.TimeUtils.convertMillisToMinutes
import kotlinx.parcelize.Parcelize

@Entity(tableName = "admin")
@Parcelize
data class Admin(
	@PrimaryKey val name: String = "",
	val password: String = "",
	var timeLimit : Long = 0L
) : Parcelable {
	fun getTimeLimitInString(): String = convertMillisToMinutes(timeLimit).toString()
}
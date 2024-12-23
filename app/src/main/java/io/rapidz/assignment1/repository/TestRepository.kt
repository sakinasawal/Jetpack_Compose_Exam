package io.rapidz.assignment1.repository

import io.rapidz.assignment1.dao.AnswerDao
import io.rapidz.assignment1.data.Answer
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

class TestRepository @Inject constructor(private val answerDao: AnswerDao) {

	suspend fun saveAnswer(answer: Answer) {
		answerDao.insertAnswer(answer)
	}

	fun getAnswers(): Flow<List<Answer>> {
		return answerDao.getAllAnswers()
	}
}
package io.rapidz.assignment1.repository

import io.rapidz.assignment1.dao.AnswerDao
import io.rapidz.assignment1.data.Answer
import javax.inject.Inject

class TestRepository @Inject constructor(private val answerDao: AnswerDao) {

	suspend fun saveAnswer(questionId: Int, answer: String) {
		answerDao.insertAnswer(Answer(questionId = questionId, answer = answer))
	}

	suspend fun getAnswer(questionId: Int): Answer? {
		return answerDao.getAnswer(questionId)
	}
}
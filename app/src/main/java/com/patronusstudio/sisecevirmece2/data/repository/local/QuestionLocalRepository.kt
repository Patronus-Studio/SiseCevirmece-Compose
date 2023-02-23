package com.patronusstudio.sisecevirmece2.data.repository.local

import android.app.Application
import android.content.Context
import com.patronusstudio.sisecevirmece2.data.abstarcts.BottleRoomDb
import com.patronusstudio.sisecevirmece2.data.model.dbmodel.QuestionDbModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuestionLocalRepository @Inject constructor(private val application: Application) {

    suspend fun addQuestions(list: MutableList<QuestionDbModel>) {
        withContext(Dispatchers.IO) {
            BottleRoomDb.getInstance(application.applicationContext).getQuestionRoomDao().addQuestions(list)
        }
    }

    suspend fun getQuestionsWithPackageId(
        localPackageId: Int
    ): List<QuestionDbModel> {
        return withContext(Dispatchers.IO) {
            BottleRoomDb.getInstance(application.applicationContext).getQuestionRoomDao().getQuestionsList(localPackageId)
        }
    }

    suspend fun removeQuestions(packageId: Int) {
        withContext(Dispatchers.IO) {
            BottleRoomDb.getInstance(application.applicationContext).getQuestionRoomDao().removeQuestions(packageId)
        }
    }

    suspend fun updateAllQuestionsShowStatus(
        localPackageId: Int,
        isShowed: Int
    ) {
        BottleRoomDb.getInstance(application.applicationContext).getQuestionRoomDao()
            .updateAllQuestionsShowStatus(isShowed, localPackageId)
    }

    suspend fun updateQuestionShowStatu(showStatu: Boolean,questionId:Int) {
        BottleRoomDb.getInstance(application.applicationContext).getQuestionRoomDao()
            .updateSingleQuestionShowStatus(showStatu, questionId)
    }

}

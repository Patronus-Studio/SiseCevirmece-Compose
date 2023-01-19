package com.patronusstudio.sisecevirmece.data.repository.local

import android.content.Context
import com.patronusstudio.sisecevirmece.data.abstarcts.BottleRoomDb
import com.patronusstudio.sisecevirmece.data.model.dbmodel.QuestionDbModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuestionLocalRepository @Inject constructor() {

    suspend fun addQuestions(context: Context, list: MutableList<QuestionDbModel>) {
        withContext(Dispatchers.IO) {
            BottleRoomDb.getInstance(context).getBottleDao().addQuestions(list)
        }
    }

    suspend fun getQuestionsWithPackageId(
        context: Context,
        localPackageId: Int
    ): List<QuestionDbModel> {
        return withContext(Dispatchers.IO) {
            BottleRoomDb.getInstance(context).getBottleDao().getQuestionsList(localPackageId)
        }
    }

    suspend fun removeQuestions(context: Context, packageId: Int) {
        withContext(Dispatchers.IO) {
            BottleRoomDb.getInstance(context).getBottleDao().removeQuestions(packageId)
        }
    }

    suspend fun updateAllQuestionsShowStatus(
        context: Context,
        localPackageId: Int,
        isShowed: Boolean
    ) {
        withContext(Dispatchers.IO) {
            BottleRoomDb.getInstance(context).getBottleDao()
                .updateAllQuestionsShowStatus(isShowed, localPackageId)
        }
    }

}

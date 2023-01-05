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

}

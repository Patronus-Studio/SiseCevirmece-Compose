package com.patronusstudio.sisecevirmece2.data.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.patronusstudio.sisecevirmece2.data.DbTables
import com.patronusstudio.sisecevirmece2.data.model.dbmodel.QuestionDbModel

@Dao
interface QuestionRoom {

    @Insert
    suspend fun insertQuestion(questionModel: QuestionDbModel): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questionModel: List<QuestionDbModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addQuestions(list: MutableList<QuestionDbModel>)

    @Query("Delete from ${DbTables.questionTable} where localPackagePrimaryId = :packageId")
    suspend fun removeQuestions(packageId: Int)

    @Query("Select * from ${DbTables.questionTable} where localPackagePrimaryId = :packagePrimaryId")
    suspend fun getQuestionsList(packagePrimaryId: Int): MutableList<QuestionDbModel>

    @Query("Update ${DbTables.questionTable} set isShowed= :isShowed where localPackagePrimaryId= :packagePrimaryId")
    suspend fun updateAllQuestionsShowStatus(isShowed: Int, packagePrimaryId: Int)

    @Query("Update ${DbTables.questionTable} set isShowed= :isShowed where primaryId= :questionPrimaryId")
    suspend fun updateSingleQuestionShowStatus(isShowed: Boolean, questionPrimaryId: Int)
}
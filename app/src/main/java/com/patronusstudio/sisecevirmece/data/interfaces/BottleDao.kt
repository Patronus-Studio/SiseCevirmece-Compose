package com.patronusstudio.sisecevirmece.data.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.patronusstudio.sisecevirmece.data.model.dbmodel.PackageDbModel
import com.patronusstudio.sisecevirmece.data.model.dbmodel.QuestionDbModel

@Dao
interface BottleDao {

    @Insert
    suspend fun insertQuestion(questionModel: QuestionDbModel):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questionModel: List<QuestionDbModel>)

    @Insert
    suspend fun insertPackage(packageModel: PackageDbModel):Long
}
package com.patronusstudio.sisecevirmece.data.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.patronusstudio.sisecevirmece.data.DbTables
import com.patronusstudio.sisecevirmece.data.model.dbmodel.PackageDbModel
import com.patronusstudio.sisecevirmece.data.model.dbmodel.QuestionDbModel

@Dao
interface BottleDao {

    @Insert
    suspend fun insertQuestion(questionModel: QuestionDbModel): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questionModel: List<QuestionDbModel>)

    @Insert
    suspend fun insertPackage(packageModel: PackageDbModel): Long

    @Query("Select * from ${DbTables.packageTable}")
    suspend fun getPackages(): List<PackageDbModel>

    @Query("Delete from ${DbTables.packageTable}")
    suspend fun removePackages()

    @Query("Delete from ${DbTables.packageTable} where cloudPackageCategoryId=:id")
    suspend fun removePackage(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPackage(model: PackageDbModel): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addQuestions(list: MutableList<QuestionDbModel>)
}